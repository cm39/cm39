package com.cm39.cm39.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 상품 판매량 집계
@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSalesQuantityDto {
    private int prodSalesQtySeq;
    private String prodNo;
    private int prodSalesQty;
    private String prodSalesQtyTypeCode;
    private LocalDateTime calcStartDate;
    private LocalDateTime calcEndDate;
}
