package com.blockeng.enums;


/**
 * 短信发送状态
 *
 * @author qiang
 */

public enum BankStatus {

    NOTUSE(0, "禁用"),
    USE(1, "使用");

    private int value;
    private String desc;

    BankStatus(final int value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return this.value;
    }

    public String getDesc() {
        return this.desc;
    }
}
