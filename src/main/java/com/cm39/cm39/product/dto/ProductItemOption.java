package com.cm39.cm39.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 품목 옵션
@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductItemOption {
    private int itemOptSeq;
    private String itemNo;
    private String prodNo;
    private String optTypeNo;
    private String optNo;
}
