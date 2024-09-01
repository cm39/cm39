package com.cm39.cm39.order.mapper;

import com.cm39.cm39.order.dto.CartProductDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CartProductMapper {
    // 장바구니 상품 저장
    int insertCartProduct(CartProductDto cartProductDto);
    // 회원 장바구니 조회
    List<CartProductDto> selectUserCart(String userId);
    // 회원 장바구니 수량 조회
    int countUserCart(String userId);
    // 상품 수량 변경
    int updateCartProductQty(CartProductDto cartProductDto);
    // 상품 삭제
    int deleteCartProduct(CartProductDto cartProductDto);

    // 테스트용
    int deleteAll();
    int countAll();
    List<CartProductDto> selectAll();
}
