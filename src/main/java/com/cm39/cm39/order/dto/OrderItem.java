package com.cm39.cm39.order.dto;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private String productNo;           // 상품 번호
    private String productName;         // 상품명
    private Integer itemPrice;          // 품목 가격
    private String itemNo;              // 품목 번호
    private Integer qty;                // 상품 수량
}