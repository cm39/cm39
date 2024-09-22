package com.cm39.cm39.order.mapper;

import com.cm39.cm39.order.dto.PaymentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaymentMapperTest {
    private final PaymentMapper paymentMapper;

    @Autowired
    PaymentMapperTest(PaymentMapper paymentMapper) {
        this.paymentMapper = paymentMapper;
    }

    @BeforeEach
    public void initTestEnvironment() {
        paymentMapper.deleteAll();
        assertThat(paymentMapper.countAll()).isEqualTo(0);
    }

    @Test
    @DisplayName("저장 성공 테스트")
    public void insertSuccessTest() {
        PaymentDto paymentDto = PaymentDto.builder()
                .payNo("P0001")
                .ordNo("ORD20240919")
                .payKey("abcdefghijklnmopqrstuvwxyz")
                .payCode("01")
                .payPrice(20000)
                .payStatCode("결제 대기")
                .dpsr("홍길동")
                .dpsrAccNo("1234-1234-1234")
                .dpsrBank("길동은행")
                .build();

        assertThat(paymentMapper.insertPayment(paymentDto))
                .isEqualTo(1);

        assertThat(paymentMapper.countAll())
                .isEqualTo(1);

        PaymentDto selectDto = paymentMapper.selectAll().get(0);

        assertThat(paymentDto)
                .isEqualTo(selectDto);
    }
}