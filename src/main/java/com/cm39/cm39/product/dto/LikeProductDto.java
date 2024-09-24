package com.cm39.cm39.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 찜 상품
@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeProductDto {
    private int likeProdSeq;
    private String userId;
    private String prodNo;
}
