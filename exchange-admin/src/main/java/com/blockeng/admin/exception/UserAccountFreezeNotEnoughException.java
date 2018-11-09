package com.blockeng.admin.exception;

public class UserAccountFreezeNotEnoughException extends Exception {
    private static final long serialVersionUID = 3805542258966557681L;

    public UserAccountFreezeNotEnoughException() {
    }

    public UserAccountFreezeNotEnoughException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public UserAccountFreezeNotEnoughException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAccountFreezeNotEnoughException(String message) {
        super(message);
    }

    public UserAccountFreezeNotEnoughException(Throwable cause) {
        super(cause);
    }
}
