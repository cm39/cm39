package com.cm39.cm39.order.service;


import com.cm39.cm39.order.dto.CartItemDto;
import com.cm39.cm39.order.vo.CartItemVo;
import com.cm39.cm39.order.mapper.CartItemMapper;
import com.cm39.cm39.order.mapper.CartItemListMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemMapper cartItemMapper;
    private final CartItemListMapper cartItemListMapper;

    // 장바구니 수량 카운트
    @Override
    public int countUserCart(String userId) {
        return cartItemMapper.countUserCart(userId);
    }

    // 장바구니에 품목 추가
    @Override
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
    @Override
    public int modifyQty(CartItemDto cartItemDto, int addQty){
        // 기존 수량 + addQty
        cartItemDto.setQty(cartItemDto.getQty() + addQty);

        // qty 검증
        validateQty(cartItemDto);

        return cartItemMapper.updateCartItem(cartItemDto);
    }

    @Override
    public void validateQty(CartItemDto cartItemDto){
        if(cartItemDto.getQty() > CartItemDto.MAX_QTY)
            cartItemDto.setQty(CartItemDto.MAX_QTY);
    }

    // 장바구니 품목 수정
    @Override
    public int modifyCartItem(CartItemDto modifyItem){
        // 1. 변경하려는 옵션의 같은 품목 조회
        CartItemDto existItemDto = cartItemMapper.selectUserCartItem(modifyItem);

        // 2. 조회 한 품목이 존재하고 품목의 시퀀스가 같지 않으면 기존 품목 수량 증가 후 삭제
        if (existItemDto != null && !existItemDto.getCartSeq().equals(modifyItem.getCartSeq())){
            // 기존 품목 수량 수정
            modifyQty(existItemDto, modifyItem.getQty());

            // 삭제
            return cartItemMapper.deleteCartItem(modifyItem);
        }

        // 3. 없거나 수량만 변경이면 수정
        return cartItemMapper.updateCartItem(modifyItem);
    }

    // 장바구니 품목 삭제
    @Override
    public int removeCartItem(CartItemDto removeItem) {
        // 기존 품목 삭제
        return cartItemMapper.deleteCartItem(removeItem);
    }

    // 장바구니 목록 조회
    @Override
    public List<CartItemVo> getUserCartList(String userId){
        return cartItemListMapper.selectUserCart(userId);
    }
}
