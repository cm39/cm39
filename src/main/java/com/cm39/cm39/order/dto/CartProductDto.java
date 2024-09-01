package com.cm39.cm39.order.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartProductDto {
    private String userId;      // 유저 아이디
    private Integer cartSeq;    // 장바구니 번호
    private String  prodNo;     // 상품 번호
    private Integer qty;        // 상품 수량
}
