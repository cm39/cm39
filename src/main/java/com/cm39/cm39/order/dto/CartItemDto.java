package com.cm39.cm39.order.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartItemDto {
    private Integer cartSeq;            // 장바구니 번호
    private String userId;              // 유저 아이디
    private String  prodNo;             // 상품 번호
    private String itemNo;              // 품목 번호
    private Integer qty;                // 상품 수량
}
