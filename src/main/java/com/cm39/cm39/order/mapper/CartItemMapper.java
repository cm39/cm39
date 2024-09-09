package com.cm39.cm39.order.mapper;

import com.cm39.cm39.order.dto.CartItemDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CartItemMapper {
    // 장바구니 상품 저장
    int insertCartItem(CartItemDto cartItemDto);
    // 회원 장바구니 조회
    List<CartItemDto> selectUserCart(String userId);
    // 회원 장바구니 조회
    CartItemDto selectUserCartItem(CartItemDto cartItemDto);
    // 회원 장바구니 수량 조회
    int countUserCart(String userId);
    // 상품 수량 변경
    int updateCartItem(CartItemDto cartItemDto);
    // 상품 삭제
    int deleteCartItem(CartItemDto cartItemDto);

    // 테스트용
    int deleteAll();
    int countAll();
    List<CartItemDto> selectAll();
}
