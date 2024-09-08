package com.cm39.cm39.order.mapper;

import com.cm39.cm39.order.dto.CartListDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CartListMapper {
    List<CartListDto> selectUserCart(String userId);
}
