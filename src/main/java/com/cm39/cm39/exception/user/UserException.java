package com.cm39.cm39.exception.user;

// 회원에서 발생하는 예외의 최상위 클래스
public class UserException extends RuntimeException {
    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }
}
