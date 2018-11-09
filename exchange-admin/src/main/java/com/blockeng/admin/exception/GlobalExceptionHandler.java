package com.blockeng.admin.exception;

import com.blockeng.admin.common.ResultMap;
import com.blockeng.framework.exception.AccountException;
import com.blockeng.framework.exception.ExchangeException;
import com.blockeng.framework.exception.GlobalDefaultException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author maple
 * @date 2018/10/14 21:24
 **/
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 全局统一处理系统业务异常，把异常的信息直接作为失败提示返给页面
     */
    @ExceptionHandler({ExchangeException.class, AccountException.class, BusinessException.class, ImgException.class, GlobalDefaultException.class})
    public ResultMap exceptionHandle(Exception e) {
        return ResultMap.getFailureResult(e.getMessage());
    }
}
