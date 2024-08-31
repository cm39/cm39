package com.cm39.cm39.exception.user;

// 이미 존재하는 계정
public class AlreadyExistsUserException extends UserException {
    public AlreadyExistsUserException(String message) {
        super(message);
    }

    public AlreadyExistsUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
