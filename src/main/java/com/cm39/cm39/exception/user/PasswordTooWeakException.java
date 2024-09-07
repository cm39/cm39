package com.cm39.cm39.exception.user;

// 보안이 취약한 비밀번호
public class PasswordTooWeakException extends UserException {
    public PasswordTooWeakException(String message) {
        super(message);
    }

    public PasswordTooWeakException(String message, Throwable cause) {
        super(message, cause);
    }
}
