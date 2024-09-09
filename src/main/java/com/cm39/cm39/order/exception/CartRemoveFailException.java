package com.cm39.cm39.order.exception;

public class CartRemoveFailException extends CartException{
    public CartRemoveFailException() {
    }

    public CartRemoveFailException(String message) {
        super(message);
    }

    public CartRemoveFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public CartRemoveFailException(Throwable cause) {
        super(cause);
    }
}
