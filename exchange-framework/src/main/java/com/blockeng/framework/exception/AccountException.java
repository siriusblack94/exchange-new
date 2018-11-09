package com.blockeng.framework.exception;

/**
 * @Description: 资金账户异常
 * @Author: Chen Long
 * @Date: Created in 2018/5/16 上午10:33
 * @Modified by: Chen Long
 */
public class AccountException extends RuntimeException {

    public AccountException() {
    }

    public AccountException(String message) {
        super(message);
    }
}
