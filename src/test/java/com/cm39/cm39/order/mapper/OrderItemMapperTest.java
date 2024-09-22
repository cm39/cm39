package com.cm39.cm39.order.mapper;

import com.cm39.cm39.order.dto.OrderItem;
import com.cm39.cm39.order.dto.OrderItemDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OrderItemMapperTest {
    private final OrderItemMapper orderItemMapper;

    @Autowired
    OrderItemMapperTest(OrderItemMapper orderItemMapper) {
        this.orderItemMapper = orderItemMapper;
    }

    @BeforeEach
    public void initTestEnvironment() {
        orderItemMapper.deleteAll();
        assertThat(orderItemMapper.countAll()).isEqualTo(0);
    }

    @Test
    @DisplayName("저장 성공 테스트")
    public void insertSuccessTest() {
        OrderItemDto orderItemDto = OrderItemDto.builder()
                .ordNo("ORD20240919")
                .ordItemSeq(1)
                .itemPrice(1000)
                .itemQty(1)
                .prodNo("P0001")
                .itemNo("I0001")
                .ordItemStatCode("주문 대기")
                .build();

        assertThat(orderItemMapper.insertOrderItem(orderItemDto))
                .isEqualTo(1);

        assertThat(orderItemMapper.countAll())
                .isEqualTo(1);

        List<OrderItemDto> selectList = orderItemMapper.selectAll();

        assertThat(orderItemDto).isEqualTo(selectList.get(0));
    }
}