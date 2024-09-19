package com.cm39.cm39.exception.user;

import com.cm39.cm39.exception.BaseException;

public class ValidationException extends BaseException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
