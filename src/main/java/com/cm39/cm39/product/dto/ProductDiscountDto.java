package com.cm39.cm39.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDiscountDto {
    private String prodDiscId;
    private String prodNo;
    private int discRate;
    private LocalDateTime discStartDate;
    private LocalDateTime discEndDate;
}
