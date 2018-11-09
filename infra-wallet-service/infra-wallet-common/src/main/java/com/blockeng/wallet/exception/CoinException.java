package com.blockeng.wallet.exception;

public class CoinException extends Exception {
    public CoinException() {
    }

    public CoinException(String msg) {
        super(msg);
    }

    public CoinException(Throwable cause) {
        super(cause);
    }

    public CoinException(String message, Throwable cause) {
        super(message, cause);
    }
}
