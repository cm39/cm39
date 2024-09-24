package com.cm39.cm39.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private String cateId;
    private String cateTypeCode;
    private String cateStatCode;
    private String cateName;
}
