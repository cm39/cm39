package com.cm39.cm39.order.service;

import com.cm39.cm39.order.dto.OrderFormRequest;
import com.cm39.cm39.order.dto.OrderFormResponse;
import com.cm39.cm39.order.dto.OrderReadyRequest;
import com.cm39.cm39.order.dto.OrderReadyResponse;
import org.springframework.transaction.annotation.Transactional;

public interface OrderService {
    @Transactional
    OrderFormResponse initOrderForm(String userId, OrderFormRequest orderFormRequest);

    OrderReadyResponse requestOrder(String userId, OrderReadyRequest request);
}
