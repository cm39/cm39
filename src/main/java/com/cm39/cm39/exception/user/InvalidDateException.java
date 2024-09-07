package com.cm39.cm39.exception.user;

// 유효하지 않은 날짜
public class InvalidDateException extends UserException {
    public InvalidDateException(String message) {
        super(message);
    }

    public InvalidDateException(String message, Throwable cause) {
        super(message, cause);
    }
}
