package com.cm39.cm39.exception.user;

import com.cm39.cm39.exception.BaseException;

public class SystemException extends BaseException {
    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
