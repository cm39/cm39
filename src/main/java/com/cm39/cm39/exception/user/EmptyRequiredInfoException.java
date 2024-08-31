package com.cm39.cm39.exception.user;

// 필수 정보 입력 누락
public class EmptyRequiredInfoException extends UserException {
    public EmptyRequiredInfoException(String message) {
        super(message);
    }

    public EmptyRequiredInfoException(String message, Throwable cause) {
        super(message, cause);
    }
}
