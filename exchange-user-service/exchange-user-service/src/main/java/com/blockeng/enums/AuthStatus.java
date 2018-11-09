package com.blockeng.enums;


/**
 * @author qiang
 */

public enum AuthStatus {

    AUTHING(0, "审核中"),
    AUTHERIZED(1, "通过"),
    AUTHENOTRIZED(2, "拒绝"),
    NOTAUTH(3, "未认证");

    private int value;
    private String desc;

    AuthStatus(final int value, final String desc) {
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
