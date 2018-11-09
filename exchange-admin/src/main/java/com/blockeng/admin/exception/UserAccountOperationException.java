package com.blockeng.admin.exception;

public class UserAccountOperationException extends Exception {
    private static final long serialVersionUID = -752980408712932994L;

    public UserAccountOperationException() {
    }

    public UserAccountOperationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public UserAccountOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAccountOperationException(String message) {
        super(message);
    }

    public UserAccountOperationException(Throwable cause) {
        super(cause);
    }
}
