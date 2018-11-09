package com.blockeng.trade.handler;

import com.blockeng.framework.exception.GlobalDefaultException;
import com.blockeng.framework.http.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(value = Exception.class)
    public Response handlerException(Exception e) {

        log.info("发生异常"+e);
        Locale locale = LocaleContextHolder.getLocale();
        GlobalDefaultException exception = (GlobalDefaultException) e;
        int code = exception.getErrcode();
        log.info("*****+code  "+code);
        String message = messageSource.getMessage(String.valueOf(code), null, "", locale);
        log.info("*****message  "+message);
        return Response.err(code, message);
    }
}