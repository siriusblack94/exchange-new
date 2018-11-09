package com.blockeng.framework.exception;

import lombok.Data;

/**
 * @author qiang
 */
@Data
public class GlobalDefaultException extends RuntimeException {

    private int errcode;

    public GlobalDefaultException(String errcode) {
        super(errcode);
        this.errcode = Integer.parseInt(errcode);
    }

    public GlobalDefaultException(int errcode) {
        super(String.valueOf(errcode));
        this.errcode = errcode;
    }
}