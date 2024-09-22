package com.cm39.cm39.order.service;

import com.cm39.cm39.exception.user.UserException;
import com.cm39.cm39.order.dto.*;
import com.cm39.cm39.order.exception.OrderException;
import com.cm39.cm39.order.mapper.OrderItemMapper;
import com.cm39.cm39.order.mapper.OrderMapper;
import com.cm39.cm39.order.vo.OrderFormItemVo;
import com.cm39.cm39.user.domain.UserDto;
import com.cm39.cm39.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.cm39.cm39.exception.user.UserExceptionMessage.*;
import static com.cm39.cm39.order.exception.OrderExceptionMessage.*;
import static com.cm39.cm39.order.service.Code.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final String SUCCESS_CALLBACK_URL = "/payment/success";
    private final String FAIL_CALLBACK_URL = "/payment/fail";

    private final UserMapper userMapper;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    private final PaymentService paymentService;

    @Override
    @Transactional
    public OrderFormResponse initOrderForm(String userId, OrderFormRequest request){
        // 주문자, 주문 품목 목록, 가용 적립금, 사용 가능 쿠폰

        // 유저 정보 조회
        UserDto user = userMapper.selectUserByUserId(userId);

        if (user == null)
            throw new UserException(ACCOUNT_NOT_FOUND.getMessage());

        // 주문 품목 목록
        List<OrderFormItemVo> orderFormItemVoList = request.getOrderFormItemVoList();

        if (orderFormItemVoList.size() < 1)
            throw new OrderException(ORDER_ITEM_NOT_FOUND.getMessage());

        return OrderFormResponse.builder()
                .user(user)
                .orderFormItemVoList(orderFormItemVoList)
                .build();
    }

    @Override
    @Transactional
    public OrderReadyResponse requestOrder(String userId, OrderReadyRequest request) {
        // 주문 정보 검증

        // 상품 할인가 비교 및 저장
        // 주문 요청 상품 리스트
        List<OrderItem> orderItemList = request.getOrderItemList();

        // 리스트의 각각 개별 상품 가격 검증
        // 총 가격 검증
        int totalOrderPrice = validateTotalOrderPrice(orderItemList);

        // 유저 아이디 저장
        request.setUserId(userId);

        // 검증 내용으로 수정
        request.setTotalOrderPrice(totalOrderPrice);

        // 주문 번호 생성
        String orderId = orderNumGenerator();

        // DB에 저장
        // 주문 테이블, 주문 상품 테이블 : 주문 대기 상태로 저장
        saveOrderInfo(OrderRequestToOrderDto(orderId, request));
        saveOrderProductInfo(OrderRequestToOrderItemDtoList(orderId, request));

        // 결제 테이블 : 결제 대기 상태로 저장
        savePaymentInfo(OrderRequestToPaymentDto(orderId, request));

        return OrderReadyResponse.builder()
                .paymentCode(request.getPayCode())
                .paymentPrice(totalOrderPrice)
                .orderId(orderId)
                .orderName(request.getOrderName())
                .customerEmail(request.getCustomerEmail())
                .customerName(request.getCustomerName())
                .customerMobilePhone(request.getCustomerMobilePhone())
                .successUrl(SUCCESS_CALLBACK_URL)
                .failUrl(FAIL_CALLBACK_URL)
                .build();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void savePaymentInfo(PaymentDto paymentDto) {
        int result = paymentService.insertPayment(paymentDto);

        if(result != 1)
            throw new OrderException(FAIL_ADD_ORDER_INFO.getMessage());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveOrderProductInfo(List<OrderItemDto> orderItemList) {
        int result = 0;
        for (OrderItemDto orderItemDto : orderItemList){
            result += orderItemMapper.insertOrderItem(orderItemDto);
        }

        if (result != orderItemList.size())
            throw new OrderException(FAIL_ADD_ORDER_INFO.getMessage());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveOrderInfo(OrderDto orderDto) {
        int result = orderMapper.insertOrder(orderDto);

        if (result != 1)
            throw new OrderException(FAIL_ADD_ORDER_INFO.getMessage());
    }

    private int validateTotalOrderPrice(List<OrderItem> orderItemList) {
        int totalOrdPrice = 0;

        for (OrderItem orderItem : orderItemList) {
            totalOrdPrice = totalOrdPrice + (orderItem.getItemPrice() * orderItem.getQty());
        }

        return totalOrdPrice;
    }

    private OrderDto OrderRequestToOrderDto(String orderId, OrderReadyRequest request){
        return OrderDto.builder()
                .ordNo(orderId)
                .userId(request.getUserId())
                .ordName(request.getOrderName())
                .totalOrdPrice(request.getTotalOrderPrice())
                .ordStatCode(ORDER_READY.getCodeName())
                .build();
    }

    private List<OrderItemDto> OrderRequestToOrderItemDtoList(String orderId, OrderReadyRequest request){
        List<OrderItemDto> result = new ArrayList<>();

        for (int i = 0; i < request.getOrderItemList().size(); i++){
            OrderItem orderItem = request.getOrderItemList().get(i);

            OrderItemDto orderItemDto = OrderItemDto.builder()
                    .ordNo(orderId)
                    .ordItemSeq(i)
                    .prodNo(orderItem.getProductNo())
                    .itemNo(orderItem.getItemNo())
                    .itemPrice(orderItem.getItemPrice())
                    .itemQty(orderItem.getQty())
                    .ordItemStatCode(ORDER_READY.getCodeName())
                    .build();

            result.add(orderItemDto);
        }

        return result;
    }

    private PaymentDto OrderRequestToPaymentDto(String orderId, OrderReadyRequest request){
        return PaymentDto.builder()
                .payNo("P" + orderId)
                .ordNo(orderId)
                .payCode(request.getPayCode())
                .payPrice(request.getTotalOrderPrice())
                .payStatCode(PAYMENT_READY.getCodeName())
                .build();
    }

    // 주문번호 생성
    private synchronized String orderNumGenerator(){
        try {
            // 쓰레드를 1mills 재운다.
            Thread.sleep(1);

            // 현재 시간 구하기
            LocalDateTime srcTime = LocalDateTime.now();

            // 포맷 정의
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSS");
            // 포맷해서 반환
            return srcTime.format(formatter);
        } catch (Exception e) {
            throw new OrderException(FAIL_ORDER_NUM_GENERATE.getMessage(), e);
        }
    }
}
