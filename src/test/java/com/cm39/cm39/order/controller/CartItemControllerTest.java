package com.cm39.cm39.order.controller;

import com.cm39.cm39.order.dto.CartItemDto;
import com.cm39.cm39.order.service.CartItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartItemController.class)
class CartItemControllerTest {

    @MockBean
    CartItemService cartItemService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    // 장바구니 수량 api 호출 테스트
    @Test
    public void apiCall_countUserCart_Test() throws Exception {
        // given
        String url = "/cart-count";
        String userId = "user1";
        given(cartItemService.countUserCart(userId)).willReturn(1);

        // when/then
        mockMvc.perform(get(url).param("userId", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(1))
                .andDo(print());
    }

    // 유저 장바구니 목록 api 호출 테스트
    @Test
    public void apiCall_userCartList_Test() throws Exception {
        // given
        String url = "/cart-item";
        String userId = "user1";

        // when/then
        mockMvc.perform(get(url).param("userId", userId))
                .andExpect(status().isOk())
                .andDo(print());
    }

    // 장바구니 품목 삭제 api 호출 테스트
    @Test
    public void apiCall_removeCart_Test() throws Exception {
        // given
        String url = "/cart-item";
        CartItemDto cartItemDto = new CartItemDto();

        given(cartItemService.removeCartItem(cartItemDto)).willReturn(1);

        // when/then
        mockMvc.perform(delete(url).content(objectMapper.writeValueAsString(cartItemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(1))
                .andDo(print());
    }

    // 장바구니 품목 수정 api 호출 테스트
    @Test
    public void apiCall_modifyCartItem_Test() throws Exception {
        // given
        String url = "/cart-item";
        CartItemDto cartItemDto = new CartItemDto();

        given(cartItemService.modifyCartItem(cartItemDto)).willReturn(1);

        // when/then
        mockMvc.perform(patch(url).content(objectMapper.writeValueAsString(cartItemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(1))
                .andDo(print());
    }

    // 장바구니 품목 추가 api 호출 테스트
    @Test
    public void apiCall_addCartItem_Test() throws Exception {
        // given
        String url = "/cart-item";
        CartItemDto cartItemDto = new CartItemDto();

        given(cartItemService.addCartItem(cartItemDto)).willReturn(1);

        // when/then
        mockMvc.perform(post(url).content(objectMapper.writeValueAsString(cartItemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(1))
                .andDo(print());
    }
}