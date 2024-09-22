package com.cm39.cm39.order.controller;

import com.cm39.cm39.order.domain.ApiResponse;
import com.cm39.cm39.order.dto.*;
import com.cm39.cm39.order.exception.PaymentException;
import com.cm39.cm39.order.service.OrderService;
import com.cm39.cm39.order.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/payment/success")
    public ResponseEntity<ApiResponse<?>> paymentSuccess(String paymentKey, String orderId, Integer amount){
        // 주문 번호로 가격 검증
        paymentService.verifyPaymentRequest(paymentKey, orderId, amount);

        // 주문 테이블, 주문 상품 테이블, 주문 상태 테이블, 결제 테이블 수정

        // 일치할 경우 TossAPI confirm 요청
        PaymentHandlerResponse response = paymentService.confirmPayment(paymentKey, orderId, amount);

        // 주문 완료 페이지 /order{주문번호} get 요청

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.builder()
                        .result("SUCCESS")
                        .data(response)
                        .build());
    }

    @GetMapping("/payment/fail")
    public ResponseEntity<ApiResponse<?>> paymentFail(String errorCode, String errorMsg, String orderId){
        // 주문 테이블, 주문 상품 테이블, 주문 상태 테이블, 결제 테이블 수정

        // 실패 메시지 반환
        PaymentHandlerFailResponse response = PaymentHandlerFailResponse.builder().build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.builder()
                        .result("FAILED")
                        .data(response)
                        .build());
    }

    @ExceptionHandler({PaymentException.class})
    public ResponseEntity<ApiResponse<?>> PaymentExceptionException(PaymentException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse
                        .builder()
                        .result("FAILED")
                        .message(ex.getMessage())
                        .build());
    }

    // 잘못된 입력 값
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ApiResponse<?>> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse
                        .builder()
                        .result("FAILED")
                        .message(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                        .build());
    }
}