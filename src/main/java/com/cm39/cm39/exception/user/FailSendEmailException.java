package com.cm39.cm39.exception.user;

public class FailSendEmailException extends UserException {
    public FailSendEmailException(String message) {
        super(message);
    }

    public FailSendEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
