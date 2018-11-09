package com.blockeng.framework.http;

import com.blockeng.framework.enums.ResultCode;
import lombok.Data;

/**
 * @author qiang
 */
@Data
public class Response implements java.io.Serializable {

    private int errcode = 0;
    private String errmsg = "ok";
    private Object data;

    public Response() {
    }

    public static Response ok(Object data) {
        return new Response(data);
    }

    public static Response ok() {
        return new Response();
    }

    public static Response err(int errcode, String errmsg) {
        return new Response(errcode, errmsg);
    }

    public static Response err(ResultCode resultCode) {

        return new Response(resultCode.getCode(), resultCode.getMessage());
    }

    private Response(Object data) {
        this.data = data;
    }

    private Response(int errcode, String errmsg) {
        this.errcode = errcode;
        this.errmsg = errmsg;
    }
}