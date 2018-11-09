package com.blockeng.framework.enums;

/**
 * @Description: 银行卡
 * @Author: Chen Long
 * @Date: Created in 2018/4/17 下午3:41
 * @Modified by: Chen Long
 */
public enum AdminBankStatus {

    ZORE(0, "无效"), ONE(1, "有效");

    private int code;
    private String name;

    AdminBankStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static AdminBankStatus getByCode(int code) {
        for (AdminBankStatus adminBankStatus : AdminBankStatus.values()) {
            if (adminBankStatus.getCode() == code) {
                return adminBankStatus;
            }
        }
        return null;
    }
}
