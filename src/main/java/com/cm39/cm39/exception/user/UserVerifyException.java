package com.cm39.cm39.exception.user;

// 인증 실패
public class UserVerifyException extends UserException {
    public UserVerifyException(String message) {
        super(message);
    }

    public UserVerifyException(String message, Throwable cause) {
        super(message, cause);
    }
}
