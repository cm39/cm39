package com.cm39.cm39.order.mapper;

import com.cm39.cm39.order.dto.OrderItemDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderItemMapper {
    int insertOrderItem(OrderItemDto orderItemDto);

    // 테스트 용
    int deleteAll();
    int countAll();
    List<OrderItemDto> selectAll();
}
