package com.cm39.cm39.order.vo;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderFormItemVo {
    private String brandId;             // 브랜드아이디
    private String brandName;           // 브랜드명
    private Integer cartSeq;            // 장바구니 번호
    private String userId;              // 유저 아이디
    private String  prodNo;             // 상품 번호
    private String itemNo;              // 품목 번호
    private String imgPath;             // 상품이미지
    private String prodName;            // 상품명
    private String itemName;            // 품목명
    private Integer basePrice;          // 정가
    private Integer discRate;           // 할인율
    private Integer qty;                // 상품 수량
}
