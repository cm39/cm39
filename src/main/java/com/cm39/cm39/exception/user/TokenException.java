package com.cm39.cm39.exception.user;

import com.cm39.cm39.exception.BaseException;

public class TokenException extends BaseException {
    public TokenException(String message) {
        super(message);
    }

    public TokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
