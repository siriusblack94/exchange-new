package com.blockeng.web.result;

import com.blockeng.framework.exception.GlobalDefaultException;
import com.blockeng.framework.http.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.AccessDeniedException;
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
        //如果是自定义的异常，返回对应的错误信息
        if (e instanceof GlobalDefaultException) {
            GlobalDefaultException exception = (GlobalDefaultException) e;
            Locale locale = LocaleContextHolder.getLocale();
            int code = exception.getErrcode();
            String message = messageSource.getMessage(String.valueOf(code), null, "", locale);
            return Response.err(code, message);
        } else if (e instanceof AccessDeniedException) {
            return Response.err(41001, "access_token missing hint.");
        } else {
            log.info(e.getMessage());
            //如果不是已知异常，返回系统繁忙
            return Response.err(-1, "系统繁忙");
        }
    }
}