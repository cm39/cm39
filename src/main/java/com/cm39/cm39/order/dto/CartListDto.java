package com.cm39.cm39.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartListDto {
    /*
        브랜드명
        배송비

        상품이미지
        상품명
        품목명
        정가
        할인율
        할인가
        수량
        주문금액

        최소주문가능수량
        최대주문가능수량
        재고수량
        배송유형
        무료배송가능금액
     */
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

    private Integer discPrice;          // 할인가
    private Integer itemTotalPrice;     // 품목 총액 discPrice * qty
    private Integer deliveryPrice;      // 배송비 : 브랜드 기준
}
