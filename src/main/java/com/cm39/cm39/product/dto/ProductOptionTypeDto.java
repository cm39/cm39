package com.cm39.cm39.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 상품옵션유형
@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductOptionTypeDto {
    private String optTypeNo;
    private String prodNo;
    private String typeName;
    private String typeDesc;
}
