package com.cm39.cm39.order.mapper;

import com.cm39.cm39.order.vo.CartItemVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CartItemListMapper {
    List<CartItemVo> selectUserCart(String userId);
}
