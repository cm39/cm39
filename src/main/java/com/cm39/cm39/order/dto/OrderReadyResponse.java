package com.cm39.cm39.order.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderReadyResponse {
    /*
        주문자 정보
        결제 정보
        구매 품목 정보
        장바구니 쿠폰 정보
        품목 쿠폰 정보
        사용 적립금 정보
        수령지 정보
     */
    private String paymentCode;                 // 결제 수단 코드
    private Integer paymentPrice;               // 결제 금액

    private String orderId;                     // 주문번호
    private String orderName;                   // 주문 이름
    private String customerEmail;               // 구매자 이메일
    private String customerName;                // 구매자 이름
    private String customerMobilePhone;         // 구매자 휴대전화번호
    private String successUrl;                  // 성공 콜백 주소
    private String failUrl;                     // 실패 콜백 주소
}
