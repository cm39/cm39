package com.cm39.cm39.product.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilter {
    private String cateId = "332000000";
    private List<ProductColors> colors;
    private int minPrice = 0;
    private int maxPrice = 15000000;
    private String isSoldOut = "N";
    private String sort = "latest";
    private int pageSize = 12; // 페이지 개수
    private int currentPage = 2; // 현재 페이지
}
