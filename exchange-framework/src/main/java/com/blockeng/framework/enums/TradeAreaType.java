package com.blockeng.framework.enums;


/**
 * 交易对type 值
 * by crow
 * 2018年4月18日17:54:35
 */
public enum TradeAreaType {

    DC_TYPE(1, "币币交易"),
    TC_TYPE(2, "创新交易");

    private int code;
    private String name;

    TradeAreaType(int code, String name) {
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

    public static TradeAreaType getByCode(int code) {
        for (TradeAreaType tradeType : TradeAreaType.values()) {
            if (tradeType.getCode() == code) {
                return tradeType;
            }
        }
        return null;
    }

}
