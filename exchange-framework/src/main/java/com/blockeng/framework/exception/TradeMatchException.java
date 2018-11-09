package com.blockeng.framework.exception;

/**
 * @Description: 币币交易撮合异常
 * @Author: Chen Long
 * @Date: Created in 2018/5/16 上午10:31
 * @Modified by: Chen Long
 */
public class TradeMatchException extends RuntimeException {

    public TradeMatchException() {
    }

    public TradeMatchException(String message) {
        super(message);
    }
}
