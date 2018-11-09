package com.blockeng.admin.exception;

public class UserAccountStatusFreezeException extends Exception {
    private static final long serialVersionUID = -5479822401913402341L;

    public UserAccountStatusFreezeException() {
    }

    public UserAccountStatusFreezeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public UserAccountStatusFreezeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAccountStatusFreezeException(String message) {
        super(message);
    }

    public UserAccountStatusFreezeException(Throwable cause) {
        super(cause);
    }
}
