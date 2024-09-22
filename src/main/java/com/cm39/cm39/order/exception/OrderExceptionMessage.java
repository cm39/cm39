package com.cm39.cm39.order.exception;

import lombok.Getter;

@Getter
public enum OrderExceptionMessage {
    FAIL_ADD_ORDER_INFO("주문 정보 저장에 실패했습니다."),
    FAIL_ORDER_NUM_GENERATE("주문 번호 생성을 실패했습니다."),
    ORDER_ITEM_NOT_FOUND("주문 품목 정보가 없습니다."),
    PAYMENT_ERROR_ORDER_AMOUNT("결제 금액이 다릅니다."),
    FAIL_JSON_TRANSLATE("JSON 변환 실패"),
    UNDEFINED_ERROR("알 수 없는 에러입니다.")
    ;

    private final String message;

    OrderExceptionMessage(String message) {
        this.message = message;
    }

}
