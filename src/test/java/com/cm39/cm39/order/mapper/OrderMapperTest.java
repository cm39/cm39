package com.cm39.cm39.order.mapper;

import com.cm39.cm39.order.dto.OrderDto;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class OrderMapperTest {
    private final OrderMapper orderMapper;

    @Autowired
    OrderMapperTest(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @BeforeEach
    public void initTestEnvironment() {
        orderMapper.deleteAll();
        int count = orderMapper.countAll();

        assertThat(count).isEqualTo(0);
    }

    @Test
    @DisplayName("주문정보 저장 성공 테스트")
    public void insertTest(){
        OrderDto orderDto = OrderDto.builder()
                .ordNo("ORD20240919")
                .userId("user1")
                .ordName("상의 외 1건")
                .totalOrdPrice(15000)
                .ordStatCode("주문 대기")
                .build();

        int result = orderMapper.insertOrder(orderDto);

        OrderDto resultDto = orderMapper.selectAll().get(0);

        assertThat(result).isEqualTo(1);
        assertThat(orderDto).isEqualTo(resultDto);
    }

    @Test
    @DisplayName("중복키 에러 테스트")
    public void insertDuplicateKeyTest() {
        OrderDto orderDto = OrderDto.builder()
                .ordNo("ORD20240919")
                .userId("user1")
                .ordName("상의 외 1건")
                .totalOrdPrice(15000)
                .ordStatCode("주문 대기")
                .build();

        assertThat(orderMapper.insertOrder(orderDto)).isEqualTo(1);
        assertThat(orderMapper.countAll()).isEqualTo(1);

        assertThatThrownBy(()->{
            orderMapper.insertOrder(orderDto);
        }).isInstanceOf(DuplicateKeyException.class);

        assertThat(orderMapper.countAll()).isEqualTo(1);
    }
}