package com.cm39.cm39.order.exception;

public class CartModifyFailException extends CartException{
    public CartModifyFailException() {
    }

    public CartModifyFailException(String message) {
        super(message);
    }

    public CartModifyFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public CartModifyFailException(Throwable cause) {
        super(cause);
    }
}
