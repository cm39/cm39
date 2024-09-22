package com.cm39.cm39.order.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentHandlerCardResponse {
    private String company;                     // "현대",
    private String number;                      // "433012******1234",
    private String installmentPlanMonths;       // 0,
    private String isInterestFree;              // false,
    private String approveNo;                   // "00000000",
    private String useCardPoint;                // false,
    private String cardType;                    // "신용",
    private String ownerType;                   // "개인",
    private String acquireStatus;               // "READY",
    private String receiptUrl;                  // "https://merchants.tosspayments.com/web/serve/merchant/test_ck_jkYG57Eba3G06EgN4PwVpWDOxmA1/receipt/5zJ4xY7m0kODnyRpQWGrN2xqGlNvLrKwv1M9ENjbeoPaZdL6"
}
