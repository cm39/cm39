package com.cm39.cm39.order.controller;

import com.cm39.cm39.order.domain.ApiResponse;
import com.cm39.cm39.order.dto.CartItemDto;
import com.cm39.cm39.order.dto.CartListDto;
import com.cm39.cm39.order.exception.CartAddFailException;
import com.cm39.cm39.order.exception.CartModifyFailException;
import com.cm39.cm39.order.exception.CartRemoveFailException;
import com.cm39.cm39.order.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartItemController {
    CartItemService cartItemService;

    @Autowired
    CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }
    /*
        장바구니 기능 로직 정리

        같은 옵션 판단 기준
        상품번호, 품목번호가 모두 같아야 한다.

        장바구니 상품 담기
        1. 처리 흐름
            1. 같은 옵션이 있는지 확인
                1. 있으면 기존 수량 + 입력 수량
                2. 없으면 입력 수량
     */

    @PostMapping("/cart-item")
    public ResponseEntity<ApiResponse<?>> addCartItem(CartItemDto cartItemDto){
        int result = cartItemService.addCartItem(cartItemDto);

        if (result != 1)
            throw new CartAddFailException("추가 실패");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse
                        .builder()
                        .result("SUCCESS")
                        .data(result)
                        .build());
    }

    @PatchMapping("/cart-item")
    public ResponseEntity<ApiResponse<?>> modifyCartItem(CartItemDto cartItemDto){
        int result = cartItemService.modifyCartItem(cartItemDto);

        if (result != 1)
            throw new CartModifyFailException("수정 실패");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse
                        .builder()
                        .result("SUCCESS")
                        .data(result)
                        .build());
    }

    @DeleteMapping("cart-item")
    public ResponseEntity<ApiResponse<?>> removeCartItem(CartItemDto cartItemDto){
        int result = cartItemService.removeCartItem(cartItemDto);

        if (result != 1)
            throw new CartRemoveFailException("삭제 실패");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse
                        .builder()
                        .result("SUCCESS")
                        .data(result)
                        .build());
    }

    @GetMapping("cart-count")
    public ResponseEntity<ApiResponse<?>> countUserCart(String userId){
        int result = cartItemService.countUserCart(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse
                        .builder()
                        .result("SUCCESS")
                        .data(result)
                        .build());
    }

    @GetMapping("cart-item")
    public ResponseEntity<ApiResponse<?>> userCartList(String userId){
        List<CartListDto> result = cartItemService.getUserCartList(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse
                        .builder()
                        .result("SUCCESS")
                        .data(result)
                        .build());
    }

    // 잘못된 입력 값
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ApiResponse<?>> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse
                        .builder()
                        .result("FAILED")
                        .message(ex.getMessage())
                        .build());
    }

    // 장바구니 품목 추가 실패
    @ExceptionHandler({CartAddFailException.class})
    public ResponseEntity<ApiResponse<?>> cartAddFailException(CartAddFailException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse
                        .builder()
                        .result("FAILED")
                        .message(ex.getMessage())
                        .build());
    }

    // 장바구니 품목 수정 실패
    @ExceptionHandler({CartModifyFailException.class})
    public ResponseEntity<ApiResponse<?>> cartModifyFailException(CartModifyFailException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse
                        .builder()
                        .result("FAILED")
                        .message(ex.getMessage())
                        .build());
    }

    // 장바구니 품목 삭제 실패
    @ExceptionHandler({CartRemoveFailException.class})
    public ResponseEntity<ApiResponse<?>> cartRemoveFailException(CartRemoveFailException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse
                        .builder()
                        .result("FAILED")
                        .message(ex.getMessage())
                        .build());
    }
}
