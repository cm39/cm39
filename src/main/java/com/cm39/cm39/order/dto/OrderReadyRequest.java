package com.cm39.cm39.order.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderReadyRequest {
    /*
        주문자 정보
        결제 정보
        구매 품목 정보
        장바구니 쿠폰 정보
        품목 쿠폰 정보
        사용 적립금 정보
        수령지 정보
     */
    private String payCode;                 // 결제 수단 코드

    private String orderName;

    private String userId;
    private String customerEmail;           // 구매자명
    private String customerName;            // 구매자명
    private String customerMobilePhone;         // 구매자 휴대전화번호
    private Integer totalOrderPrice;        // 총 주문금액

    private List<OrderItem> orderItemList;  // 주문 품목 목록

    private void makeOrderName(){
        orderName = orderItemList.get(0).getProductName() + " 외 " + (orderItemList.size() - 1) + "건";
    }
    public String getOrderName(){
        if (orderName == null)
            makeOrderName();
        return orderName;
    }
}