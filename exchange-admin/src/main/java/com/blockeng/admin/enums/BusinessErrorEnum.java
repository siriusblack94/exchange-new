package com.blockeng.admin.enums;

/**
 * Created by Haliyo on 2018-5-21
 */
public enum BusinessErrorEnum {
    //公共错误
    PARAMETERS_ERROR("10", "参数有误"),


    COIN_WITHDRAW_ERROR("101", "提现审核异常"),

    ;
    private String code;
    private String message;

    BusinessErrorEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static BusinessErrorEnum fromCode(String code) {
        if (code == null) return null;

        for (BusinessErrorEnum businessErrorEnum : values()) {
            if (businessErrorEnum.getCode().equals(code)) {
                return businessErrorEnum;
            }
        }
        return null;
    }
}
