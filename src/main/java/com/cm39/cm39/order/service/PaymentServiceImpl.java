package com.cm39.cm39.order.service;

import com.cm39.cm39.exception.user.SystemException;
import com.cm39.cm39.order.dto.*;
import com.cm39.cm39.order.exception.PaymentException;
import com.cm39.cm39.order.mapper.OrderItemMapper;
import com.cm39.cm39.order.mapper.OrderMapper;
import com.cm39.cm39.order.mapper.PaymentMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static com.cm39.cm39.order.exception.OrderExceptionMessage.*;
import static com.cm39.cm39.order.service.Code.*;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentMapper paymentMapper;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public int insertPayment(PaymentDto paymentDto) {
        return paymentMapper.insertPayment(paymentDto);
    }

    @Override
    public void verifyPaymentRequest(String paymentKey, String orderId, Integer amount) {
        PaymentDto payment = paymentMapper.selectPaymentByOrderNo(orderId);

        if(payment == null)
            throw new PaymentException(UNDEFINED_ERROR.getMessage());

        if (!payment.getPayPrice().equals(amount))
            throw new PaymentException(PAYMENT_ERROR_ORDER_AMOUNT.getMessage());

        payment.setPayKey(paymentKey);
        paymentMapper.updatePaymentKey(payment);
    }

    @Override
    public PaymentHandlerResponse confirmPayment(String paymentKey, String orderId, Integer amount) {
        RestClient restClient = RestClient.create();

        Map<String, Object> map = new HashMap<>();
        map.put("paymentKey", paymentKey);
        map.put("orderId", orderId);
        map.put("amount", amount);

        String widgetSecretKey = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((widgetSecretKey + ":").getBytes(StandardCharsets.UTF_8));
        String authorizations = "Basic " + new String(encodedBytes);

        try {
            return restClient.post()
                    .uri("https://api.tosspayments.com/v1/payments/confirm")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", authorizations)
                    .body(objectMapper.writeValueAsString(map))
                    .retrieve()
                    .onStatus(HttpStatusCode::is2xxSuccessful, (request, response) -> {
                        // 주문, 주문상품, 결제 수정
                        orderMapper.updateOrderStatusByOrderNo(OrderDto.builder()
                                .ordNo(orderId)
                                .ordStatCode(ORDER_SUCCESS.getCodeName())
                                .build());

                        orderItemMapper.updateOrderItemStatusByOrderNo(OrderItemDto.builder()
                                .ordNo(orderId)
                                .ordItemStatCode(ORDER_SUCCESS.getCodeName())
                                .build());

                        paymentMapper.updatePaymentStatusByOrderNo(PaymentDto.builder()
                                .ordNo(orderId)
                                .payStatCode(PAYMENT_SUCCESS.getCodeName())
                                .build());
                    })
                    .onStatus(HttpStatusCode::isError, (request, response) -> {     // 결제가 실패 하면
                        PaymentHandlerFailResponse failResponse = objectMapper.readValue(response.getBody(), PaymentHandlerFailResponse.class);
                        // 결제 예외 발생
                        throw new PaymentException(failResponse.getMessage());
                    })
                    .toEntity(PaymentHandlerResponse.class)
                    .getBody();
        } catch (JsonProcessingException e) {
            throw new SystemException(FAIL_JSON_TRANSLATE.getMessage(), e);
        }
    }
}