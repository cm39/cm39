package com.cm39.cm39.order.mapper;

import com.cm39.cm39.order.dto.OrderDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {
    int insertOrder(OrderDto orderDto);

    int updateOrderStatusByOrderNo(OrderDto orderDto);

    // 테스트 용
    int deleteAll();
    int countAll();
    List<OrderDto> selectAll();
}
