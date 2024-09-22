package com.cm39.cm39.order.service;

import com.cm39.cm39.order.dto.PaymentDto;
import com.cm39.cm39.order.dto.PaymentHandlerResponse;

public interface PaymentService {
    int insertPayment(PaymentDto paymentDto);
    void verifyPaymentRequest(String paymentKey, String orderId, Integer amount);
    PaymentHandlerResponse confirmPayment(String paymentKey, String orderId, Integer amount);
}
