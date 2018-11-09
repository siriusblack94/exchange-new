package com.blockeng.extend.handler;

import com.blockeng.extend.exception.GlobalException;

import com.blockeng.framework.http.Response;

import lombok.extern.slf4j.Slf4j;


import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler {


    @ExceptionHandler(value = Exception.class)
    public Response handlerException(Exception e) throws Exception{

        if (e instanceof GlobalException){
                return Response.err(403, e.getMessage());
        }
        log.error("",e);
        return Response.err(500,"访问拒绝");
    }
}