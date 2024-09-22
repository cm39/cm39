package com.cm39.cm39.order.mapper;

import com.cm39.cm39.order.dto.OrderDto;
import com.cm39.cm39.order.dto.PaymentDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PaymentMapper {
    int insertPayment(PaymentDto paymentDto);
    PaymentDto selectPaymentByOrderNo(String ordNo);
    int updatePaymentKey(PaymentDto paymentDto);

    int updatePaymentStatusByOrderNo(PaymentDto paymentDto);

    // 테스트 용
    int deleteAll();
    int countAll();
    List<PaymentDto> selectAll();
}
