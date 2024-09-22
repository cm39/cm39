package com.cm39.cm39.order.service;

import com.cm39.cm39.order.dto.OrderItem;
import com.cm39.cm39.order.dto.OrderReadyRequest;
import com.cm39.cm39.order.mapper.OrderItemMapper;
import com.cm39.cm39.order.mapper.OrderMapper;
import com.cm39.cm39.order.mapper.PaymentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class OrderServiceImplTransactionalTest {
    private OrderService orderService;
    private OrderMapper orderMapper;
    private OrderItemMapper orderItemMapper;
    private PaymentMapper paymentMapper;

    @Autowired
    public OrderServiceImplTransactionalTest(OrderService orderService, OrderMapper orderMapper, OrderItemMapper orderItemMapper, PaymentMapper paymentMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.paymentMapper = paymentMapper;
    }

    @BeforeEach
    public void initTestEnvironment() {
        orderMapper.deleteAll();
        orderItemMapper.deleteAll();
        paymentMapper.deleteAll();

        assertThat(orderMapper.countAll()).isEqualTo(0);
        assertThat(orderItemMapper.countAll()).isEqualTo(0);
        assertThat(paymentMapper.countAll()).isEqualTo(0);
    }

    @Test
    @DisplayName("트랜잭션 롤백 테스트")
    public void transactionRollbackTest() {
        List<OrderItem> list = new ArrayList<>();

        OrderItem orderItem1 = OrderItem.builder()
                .productNo("P0001")
                .itemNo("I0001")
                .itemPrice(5000)
                .qty(2)
                .build();

        OrderItem orderItem2 = OrderItem.builder()
                .productNo("P0001")
                .itemNo("I0001")
                .itemPrice(2500)
                .qty(2)
                .build();

        list.add(orderItem1);
        list.add(orderItem2);

        OrderReadyRequest request = OrderReadyRequest.builder()
                .payCode("01")
                .totalOrderPrice(15000)
                .orderItemList(list)
                .build();

        orderService.requestOrder("user1", request);
    }
}