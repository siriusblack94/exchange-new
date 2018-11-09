package com.blockeng.admin.exception;

/**
 * @Auther: sirius
 * @Date: 2018/10/30 14:46
 * @Description:
 */
public class RequestLimitException extends Exception {
        private static final long serialVersionUID = 1364225358754654702L;

        public RequestLimitException(){
            super("HTTP请求无效");
        }

        public RequestLimitException(String message) {
            super(message);
        }
}
