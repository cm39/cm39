package com.cm39.cm39.order.exception;

import lombok.Getter;

@Getter
public enum CartExceptionMessage {
    FAIL_REMOVE_CART_ITEM("삭제를 실패했습니다."),
    FAIL_ADD_CART_ITEM("추가를 실패했습니다."),
    FAIL_MODIFY_CART_ITEM("수정을 실패했습니다."),
    EMPTY_REQUIRED_FIELD("필수 입력 정보가 비었습니다."),
    INVALID_QTY("유효 하지 않은 수량입니다.");

    private final String message;

    CartExceptionMessage(String message) {
        this.message = message;
    }

}
