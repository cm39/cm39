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
public class ProductReviewDto {
    private int revSeq;
    private String prodNo;
    private String userId;
    private String mngrId;
    private String revStateCode;
    private String content;
    private int starRate;
    private int viewQty;
    private int likeQty;
    private double height;
    private double weight;
    private LocalDateTime revChkDate;
}
