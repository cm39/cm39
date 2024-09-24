package com.cm39.cm39.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionDto {
    private String optNo;
    private String optTypeNo;
    private String optName;
    private int sortOrd; // 정렬순서
}
