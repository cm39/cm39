package com.cm39.cm39.order.service;

import lombok.Getter;

@Getter
public enum Code {
    ORDER_READY("001", "주문대기"),
    ORDER_SUCCESS("001", "주문완료"),
    ORDER_CANCEL("001", "주문취소"),
    ORDER_FAIL("001", "주문실패"),

    PAYMENT_READY("001", "결제대기"),
    PAYMENT_SUCCESS("001", "결제완료"),
    PAYMENT_DEPOSIT_WAIT("001", "입금대기"),
    PAYMENT_DEPOSIT_CONFIRMATION("001", "입금확인"),
    PAYMENT_CANCEL("001", "결제취소"),
    PAYMENT_FAIL("001", "결제실패"),
    ;

    private final String Code;
    private final String CodeName;

    Code(String code, String codeName) {
        Code = code;
        CodeName = codeName;
    }
}
