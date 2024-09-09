package com.cm39.cm39.exception.user;

// 유효하지 않은 입력 형식
public class InvalidInputFormatException extends UserException {
    public InvalidInputFormatException(String message) {
        super(message);
    }

    public InvalidInputFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
