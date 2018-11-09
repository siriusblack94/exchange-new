package com.blockeng.framework.enums;

public enum PriceTradeType {

    PRICEONE_ONE("11", "买入开仓"),
    PRICEONE_TWO("12", "买入平仓"),
    TRADETWO_ONE("21", "卖出开仓"),
    TRADETWO_TWO("22", "卖出平仓");

    private String code;
    private String name;

    PriceTradeType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getByCode(String code) {
        for (PriceTradeType tradeType : PriceTradeType.values()) {
            if (tradeType.getCode().equals(code)) {
                return tradeType.getName();
            }
        }
        return "";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
