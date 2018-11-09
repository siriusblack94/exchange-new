package com.blockeng.extend.exception;

/**
 * @Auther: sirius
 * @Date: 2018/11/5 11:54
 * @Description:
 */
public class GlobalException  extends RuntimeException{

    public GlobalException(){
        super("HTTP请求无效");
    }

    public GlobalException(String message) {
        super(message);
    }
}
