package com.blockeng.web.result;

import com.blockeng.framework.exception.GlobalDefaultException;
import com.blockeng.framework.http.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

/**
 * @author qiang
 */
@RestControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(value = Exception.class)
    public Response handlerException(Exception e) {
        Locale locale = LocaleContextHolder.getLocale();
        //如果是自定义的异常，返回对应的错误信息
        if (e instanceof GlobalDefaultException) {
            GlobalDefaultException exception = (GlobalDefaultException) e;
            int code = exception.getErrcode();
            String message = messageSource.getMessage(String.valueOf(code), null, "", locale);
            return Response.err(code, message);
        } else if (e instanceof AccessDeniedException) {
            String message = messageSource.getMessage(String.valueOf(41001), null, "", locale);
            return Response.err(41001, message);
        } else if (e instanceof BadCredentialsException) {
            String message = messageSource.getMessage(String.valueOf(2021), null, "", locale);
            return Response.err(2021, message);
        } else {
            //如果不是已知异常，返回系统异常
            log.info(e.getMessage());
            return Response.err(-1, "系统异常");
        }
    }
}