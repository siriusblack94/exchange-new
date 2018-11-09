package com.blockeng.admin.exception;

import com.blockeng.admin.enums.BusinessErrorEnum;

import java.text.MessageFormat;
import java.util.Map;

/**
 * Created by Haliyo on 2018-5-21
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误信息模板
     */
    private final static String MSG_TEMPLATE = "错误码:{0}, 描述:{1}";
    private final static String MSG_FULL_TEMPLATE = "错误码:{0}, 描述:{1}, 异常信息:{2}";
    private Map<String, Integer> extMap;
    private String exMsg;

    public BusinessException(BusinessErrorEnum businessErrorEnum) {
        super(MessageFormat.format(MSG_TEMPLATE
                , businessErrorEnum.getCode(), businessErrorEnum.getMessage()));
        this.businessErrorEnum = businessErrorEnum;
    }

    private BusinessErrorEnum businessErrorEnum;

    public BusinessException(BusinessErrorEnum businessErrorEnum, String exMsg) {
        super(MessageFormat.format(MSG_FULL_TEMPLATE
                , businessErrorEnum.getCode(), businessErrorEnum.getMessage(), exMsg));
        this.businessErrorEnum = businessErrorEnum;
        this.exMsg = exMsg;
    }

    public BusinessException(BusinessErrorEnum businessErrorEnum, Map<String, Integer> extMap) {
        super(MessageFormat.format(MSG_FULL_TEMPLATE
                , businessErrorEnum.getCode(), businessErrorEnum.getMessage(), extMap));
        this.businessErrorEnum = businessErrorEnum;
        this.extMap = extMap;
    }

    public BusinessErrorEnum getBusinessErrorEnum() {
        return businessErrorEnum;
    }

    public Map<String, Integer> getExtMap() {
        return extMap;
    }

    public void setExtMap(Map<String, Integer> extMap) {
        this.extMap = extMap;
    }

    public String getExMsg() {
        return exMsg;
    }
}
