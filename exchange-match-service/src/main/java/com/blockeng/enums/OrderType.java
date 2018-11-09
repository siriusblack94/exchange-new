package com.blockeng.enums;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/5/16 上午11:05
 * @Modified by: Chen Long
 */
public enum OrderType {

    BUY(1, "买"),
    SELL(2, "卖");

    private int code;
    private String desc;

    OrderType(final int code, final String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    public static OrderType getByCode(int code) {
        for (OrderType orderType : OrderType.values()) {
            if (orderType.getCode() == code) {
                return orderType;
            }
        }
        return null;
    }
}
