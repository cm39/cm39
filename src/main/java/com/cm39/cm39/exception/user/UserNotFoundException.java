package com.cm39.cm39.exception.user;

// 존재하지 않는 유저
public class UserNotFoundException extends UserException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
