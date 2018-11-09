package com.blockeng.framework.enums;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/4/29 下午5:35
 * @Modified by: Chen Long
 */
public enum UserType {

    CUSTOMER(1, "普通用户"),
    AGENT(2, "代理人");

    private int code;
    private String desc;

    UserType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static UserType getByCode(int code) {
        for (UserType userType : UserType.values()) {
            if (code == userType.getCode()) {
                return userType;
            }
        }
        return null;
    }
}
