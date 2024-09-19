package com.cm39.cm39.order.service;

import com.cm39.cm39.order.dto.PaymentDto;
import com.cm39.cm39.order.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int insertPayment(PaymentDto paymentDto) {
        return paymentMapper.insertPayment(paymentDto);
    }
}
