package com.cm39.cm39.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 상품옵션
@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductOptionDto {
    private String optNo;
    private String optTypeNo;
    private String prodNo;
    private String optName;
    private int sortOrd; // 정렬순서
}
