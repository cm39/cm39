package com.cm39.cm39.order.service;


import com.cm39.cm39.order.dto.CartItemDto;
import com.cm39.cm39.order.dto.CartListDto;
import com.cm39.cm39.order.mapper.CartItemMapper;
import com.cm39.cm39.order.mapper.CartListMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemService {
    private final CartItemMapper cartItemMapper;
    private final CartListMapper cartListMapper;

    // 장바구니 수량 카운트
    public int countUserCart(String userId) {
        return cartItemMapper.countUserCart(userId);
    }

    // 장바구니에 품목 추가
    public int addCartItem(CartItemDto addItem){
        // 1. 같은 품목 조회
        CartItemDto existItemDto = cartItemMapper.selectUserCartItem(addItem);

        // 1-1. 없으면 저장
        if (existItemDto == null)
            return cartItemMapper.insertCartItem(addItem);

        // 1-2. 있으면 기존 수량 + n 수정
        return modifyQty(existItemDto, addItem.getQty());
    }

    // 수량 변경
    public int modifyQty(CartItemDto cartItemDto, int addQty){
        // 기존 수량 + addQty
        cartItemDto.setQty(cartItemDto.getQty() + addQty);
        return cartItemMapper.updateCartItem(cartItemDto);
    }

    // 장바구니 품목 수정
    @Transactional
    public int modifyCartItem(CartItemDto modifyItem){
        // 1. 변경하려는 옵션의 같은 품목 조회
        CartItemDto existItemDto = cartItemMapper.selectUserCartItem(modifyItem);

        // 1-1. 없으면 수정
        if (existItemDto == null)
            return cartItemMapper.updateCartItem(modifyItem);

        // 1-2 있으면 수량만 변경
        return modifyQty(existItemDto, modifyItem.getQty());
    }

    // 장바구니 품목 삭제
    @Transactional
    public int removeCartItem(CartItemDto removeItem) {
        // 기존 품목 삭제
        return cartItemMapper.deleteCartItem(removeItem);
    }

    // 장바구니 목록 조회
    public List<CartListDto> getUserCartList(String userId){
        return cartListMapper.selectUserCart(userId);
    }
}
