package com.blockeng.framework.enums;

/**
 * @Description: 交易类型
 * @Author: Chen Long
 * @Date: Created in 2018/4/17 下午3:36
 * @Modified by: Chen Long
 */
public enum TradeType {

    OPEN_POSITION(1, "开仓"), CLOSE_POSITION(2, "平仓");

    private int code;
    private String name;

    TradeType(int code, String name) {
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

    public static TradeType getByCode(int code) {
        for (TradeType tradeType : TradeType.values()) {
            if (tradeType.getCode() == code) {
                return tradeType;
            }
        }
        return null;
    }
}
