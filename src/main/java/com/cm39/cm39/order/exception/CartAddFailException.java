package com.cm39.cm39.order.exception;

public class CartAddFailException extends CartException{
    public CartAddFailException() {
    }

    public CartAddFailException(String message) {
        super(message);
    }

    public CartAddFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public CartAddFailException(Throwable cause) {
        super(cause);
    }
}
