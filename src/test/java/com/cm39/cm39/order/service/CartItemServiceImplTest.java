package com.cm39.cm39.order.service;

import com.cm39.cm39.order.dto.CartItemDto;
import com.cm39.cm39.order.vo.CartItemVo;
import com.cm39.cm39.order.mapper.CartItemMapper;
import com.cm39.cm39.order.mapper.CartItemListMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CartItemServiceImplTest {
    @Mock
    CartItemMapper cartItemMapper;

    @Mock
    CartItemListMapper cartItemListMapper;

    @InjectMocks
    CartItemService cartItemService;

        /*
        장바구니 기능
        1. 장바구니에 상품 담기
        2. 장바구니 상품 품목 수정
        3. 장바구니 품목 삭제
        4. 장바구니 목록 조회

        1. 장바구니에 상품 담기
            1. 기존에 상품이 있는지 확인
            2. 있으면 수량 증가 후 수정
            3. 없으면 새로 저장
     */
    /*
        장바구니에 상품 담기 테스트

        같은_품목이_없으면_insertCartItem_호출
        같은_품목이_있으면_updateCartItem_호출
        result_1이_아니면_Exception_발생
     */

    @Test
    @DisplayName("같은_품목이_없으면_insertCartItem_호출")
    public void 같은_품목이_없으면_insertCartItem_호출() {
        // given
        CartItemDto cartItemDto = new CartItemDto();

        doReturn(null)
                .when(cartItemMapper)
                .selectUserCartItem(any(CartItemDto.class));

        doReturn(1)
                .when(cartItemMapper)
                .insertCartItem(any(CartItemDto.class));

        // when
        int result = cartItemService.addCartItem(cartItemDto);

        // then
        assertEquals(result, 1);

        // verify
        verify(cartItemMapper, times(1)).selectUserCartItem(any(CartItemDto.class));
        verify(cartItemMapper, times(1)).insertCartItem(any(CartItemDto.class));
    }

    @Test
    @DisplayName("같은_품목이_있으면_updateCartItem_호출")
    public void 같은_품목이_있으면_updateCartItem_호출() {
        // given
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setQty(1);

        doReturn(cartItemDto)
                .when(cartItemMapper)
                .selectUserCartItem(any(CartItemDto.class));

        doReturn(1)
                .when(cartItemMapper)
                .updateCartItem(any(CartItemDto.class));

        // when
        int result = cartItemService.addCartItem(cartItemDto);

        // then
        assertEquals(result, 1);

        // verify
        verify(cartItemMapper, times(1)).selectUserCartItem(any(CartItemDto.class));
        verify(cartItemMapper, times(1)).updateCartItem(any(CartItemDto.class));
    }

    @Test
    @DisplayName("수정 테스트")
    public void modifyTest() {
        // given
        CartItemDto cartItemDto = new CartItemDto();
        when(cartItemMapper.updateCartItem(cartItemDto))
                .thenReturn(1);

        // when
        int result = cartItemService.modifyCartItem(cartItemDto);

        // then
        assertEquals(result, 1);

        // verify
        verify(cartItemMapper, times(1)).updateCartItem(any(CartItemDto.class));
    }

    @Test
    @DisplayName("삭제 테스트")
    public void removeTest() {
        // given
        CartItemDto cartItemDto = new CartItemDto();
        when(cartItemMapper.deleteCartItem(cartItemDto))
                .thenReturn(1);

        // when
        int result = cartItemService.removeCartItem(cartItemDto);

        // then
        assertEquals(result, 1);

        // verify
        verify(cartItemMapper, times(1)).deleteCartItem(any(CartItemDto.class));
    }

    @Test
    @DisplayName("장바구니 수량 카운트 테스트")
    public void userCartCountTest() {
        // given
        String userId = "user1";
        when(cartItemMapper.countUserCart(userId))
                .thenReturn(1);

        // when
        int result = cartItemService.countUserCart(userId);

        // then
        assertEquals(result, 1);

        // verify
        verify(cartItemMapper, times(1)).countUserCart(any(String.class));
    }

    @Test
    @DisplayName("유저 장바구니 테스트")
    public void userCartListTest() {
        // given
        List<CartItemVo> cartListDtoList = new ArrayList<>();
        String userId = "user1";
        when(cartItemListMapper.selectUserCart(userId))
                .thenReturn(cartListDtoList);

        // when
        List<CartItemVo> result = cartItemService.getUserCartList(userId);

        // then
        assertEquals(result.size(), 0);

        // verify
        verify(cartItemListMapper, times(1)).selectUserCart(any(String.class));
    }

//    @Test
//    @DisplayName("result_1이_아니면_CartAddFailException_발생")
//    public void result_1이_아니면_CartAddFailException_발생() {
//        // given
//        CartItemDto cartItemDto = new CartItemDto();
//        cartItemDto.setQty(0);
//
//        doReturn(null)
//                .when(cartItemMapper)
//                .selectUserCartItem(any(CartItemDto.class));
//
//        doReturn(0)
//                .when(cartItemMapper)
//                .insertCartItem(any(CartItemDto.class));
//
//        doReturn(0)
//                .when(cartItemMapper)
//                .updateCartItem(any(CartItemDto.class));
//
//        when(cartItemService.modifyQty(cartItemDto, 0)).thenReturn(0);
//
//        // when/then
//        assertThrows(CartAddFailException.class, () -> {
//            cartItemService.addCartItem(cartItemDto);
//        });
//
//
//        // verify
//        verify(cartItemMapper, times(1)).selectUserCartItem(any(CartItemDto.class));
//        verify(cartItemMapper, times(1)).insertCartItem(any(CartItemDto.class));
//    }
//
//    @Test
//    @DisplayName("result_1이_아니면_CartModifyException_발생")
//    public void result_1이_아니면_CartModifyException_발생() {
//        // given
//        CartItemDto cartItemDto = new CartItemDto();
//
//        doReturn(0)
//                .when(cartItemMapper)
//                .updateCartItem(any(CartItemDto.class));
//
//        // when/then
//        assertThrows(CartModifyFailException.class, () -> {
//            cartItemService.modifyCartItem(cartItemDto);
//        });
//
//        // verify
//        verify(cartItemMapper, times(1)).updateCartItem(any(CartItemDto.class));
//    }
//
//    @Test
//    @DisplayName("result_1이_아니면_CartRemoveException_발생")
//    public void result_1이_아니면_CartRemoveException_발생() {
//        // given
//        CartItemDto cartItemDto = new CartItemDto();
//
//        when(cartItemMapper.deleteCartItem(cartItemDto))
//                .thenReturn(0);
//
//        // when/then
//        assertThrows(CartRemoveFailException.class, () -> {
//            cartItemService.removeCartItem(cartItemDto);
//        });
//
//        // verify
//        verify(cartItemMapper, times(1)).deleteCartItem(any(CartItemDto.class));
//    }
}