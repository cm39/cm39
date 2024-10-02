// product o
// review o
// category o
// product discount o
// product sales quantity o
// like product o
// option type - 옵션 유형
// option - 옵션
// product option type - 상품 옵션 유형
// product option - 상품 옵션
// product item - 품목 o
// product item option - 품목 옵션

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
public class ProductDto {
    private String prodNo;
    private String cateId;
    private String brandId;
    private String prodStatCode;
    private String prodName;
    private String prodImg;
    private int starRate;
    private int revQty;
    private int likeQty;
    private String isOwnProd; // 자사상품여부
    private int basePrice;
    private int prodViewQty;
    private String prodDesc;
    private String dlvInfo;
    private String prodMat; // 상품소재
    private String prodSize;
    private String mfrName; // 제조사명
    private String mfrCtry; // 제조국
    private String mfrDate; // 제조연월
    private String caut; // 주의사항
    private String qaStd; // 품질보증기준
    private String asInfo; // A/S 정보
    private LocalDateTime prodRegDate; // 상품등록일시
    private int dlvLeadTime; // 배송소요시간
}
