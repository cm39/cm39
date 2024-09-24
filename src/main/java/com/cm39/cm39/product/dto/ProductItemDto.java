package com.cm39.cm39.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 품목
@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductItemDto {
    private String itemNo;
    private String prodNo;
    private String itemName;
    private int stockQty;
    private int stockSftyQty; // 안전재고수량
    private int minOrdQty; // 최소주문가능수량
    private int maxOrdQty; // 최대주문가능수량
    private String isDsply; // 진열여부
    private String isSalePosbl; // 판매가능여부
    private int addAmt; // 추가금액
}
