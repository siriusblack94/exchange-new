package com.blockeng.admin.exception;

public class UserAccountBalanceNotEnoughException extends Exception {
    private static final long serialVersionUID = 1042423248903342982L;

    public UserAccountBalanceNotEnoughException() {
    }

    public UserAccountBalanceNotEnoughException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public UserAccountBalanceNotEnoughException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAccountBalanceNotEnoughException(String message) {
        super(message);
    }

    public UserAccountBalanceNotEnoughException(Throwable cause) {
        super(cause);
    }
}
