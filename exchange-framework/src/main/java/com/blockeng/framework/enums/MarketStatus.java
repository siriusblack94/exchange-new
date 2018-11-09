package com.blockeng.framework.enums;

/**
 * 创新交易市场状态信息
 * 状态：1-启用；0禁用
 *
 * @author crow
 */

public enum MarketStatus {

    UN_USER(0, "禁用"),
    USER(1, "启用");

    private int value;
    private String desc;

    MarketStatus(final int value, final String desc) {
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