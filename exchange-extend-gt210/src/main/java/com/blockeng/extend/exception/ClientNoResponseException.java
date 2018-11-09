package com.blockeng.extend.exception;


public class ClientNoResponseException extends GlobalException {

    public ClientNoResponseException(){super("接口无响应");}

    public ClientNoResponseException(String message){super(message);}
}
