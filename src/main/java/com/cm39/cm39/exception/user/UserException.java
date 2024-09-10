package com.cm39.cm39.exception.user;

import com.cm39.cm39.exception.BaseException;

public class UserException extends BaseException {
    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }
}
