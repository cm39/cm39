package com.cm39.cm39.order.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentHandlerResponse {
    private String mId;                         // "tosspayments", 가맹점 ID
    private String version;                     // "1.3", Payment 객체 응답 버전
    private String paymentKey;                  // "5zJ4xY7m0kODnyRpQWGrN2xqGlNvLrKwv1M9ENjbeoPaZdL6",
    private String orderId;                     // "IBboL1BJjaYHW6FA4nRjm",
    private String orderName;                   // "토스 티셔츠 외 2건",
    private String currency;                    // "KRW",
    private String method;                      // "카드", 결제수단
    private String totalAmount;                 // 15000,
    private String balanceAmount;               // 15000,
    private String suppliedAmount;              // 13636,
    private String vat;                         // 1364,
    private String status;                      // "DONE", 결제 처리 상태
    private String requestedAt;                 // "2021-01-01T10:01:30+09:00",
    private String approvedAt;                  // "2021-01-01T10:05:40+09:00",
    private String useEscrow;                   // false,
    private String cultureExpense;              // false,
    private PaymentHandlerCardResponse card;    // 카드
    private String type;                        // "NORMAL", 결제 타입 정보 (NOMAL, BILLING, CONNECTPAY)
}