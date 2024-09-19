package com.cm39.cm39.order.service;

import com.cm39.cm39.order.dto.CartItemDto;
import com.cm39.cm39.order.vo.CartItemVo;

import java.util.List;

public interface CartItemService {
    // 장바구니 수량 카운트
    int countUserCart(String userId);

    // 장바구니에 품목 추가
    int addCartItem(CartItemDto addItem);

    // 수량 변경
    int modifyQty(CartItemDto cartItemDto, int addQty);

    void validateQty(CartItemDto cartItemDto);

    // 장바구니 품목 수정
    int modifyCartItem(CartItemDto modifyItem);

    // 장바구니 품목 삭제
    int removeCartItem(CartItemDto removeItem);

    // 장바구니 목록 조회
    List<CartItemVo> getUserCartList(String userId);
}
