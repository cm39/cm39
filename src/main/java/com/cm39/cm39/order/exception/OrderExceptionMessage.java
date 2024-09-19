package com.cm39.cm39.order.exception;

import lombok.Getter;

@Getter
public enum OrderExceptionMessage {
    FAIL_ADD_ORDER_INFO("주문 정보 저장에 실패했습니다."),
    FAIL_ORDER_NUM_GENERATE("주문 번호 생성을 실패했습니다."),
    ORDER_ITEM_NOT_FOUND("주문 품목 정보가 없습니다.")
    ;

    private final String message;

    OrderExceptionMessage(String message) {
        this.message = message;
    }

}
