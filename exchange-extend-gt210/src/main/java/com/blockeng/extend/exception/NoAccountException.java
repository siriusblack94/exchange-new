package com.blockeng.extend.exception;

import com.blockeng.framework.exception.GlobalDefaultException;

/**
 * @Auther: sirius
 * @Date: 2018/10/31 15:01
 * @Description:
 */
public class NoAccountException extends GlobalException {
    private static final long serialVersionUID = 1364225358754654701L;

    public NoAccountException(){
        super("HTTP请求无效");
    }

    public NoAccountException(String message) {
        super(message);
    }
}
