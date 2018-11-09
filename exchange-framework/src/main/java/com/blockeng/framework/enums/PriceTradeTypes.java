package com.blockeng.framework.enums;

public enum PriceTradeTypes {

    //一 1市价2现价  二 1开仓2平仓  三 1买入2卖出
    PRICEONE_ONE_ONE("111", "市价开仓买入"),
    PRICEONE_ONE_TWO("112", "市价开仓卖出"),
    PRICEONE_TWO_ONE("121", "市价平仓买入"),
    PRICEONE_TWO_TWO("122", "市价平仓卖出"),
    TYPETWO_ONE_ONE("211", "现价开仓买入"),
    TYPETWO_ONE_TWO("212", "现价开仓卖出"),
    TYPETWO_TWO_ONE("221", "现价平仓买入"),
    TYPETWO_TWO_TWO("222", "现价平仓卖出");

    private String code;
    private String name;

    PriceTradeTypes(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getByCode(String code) {
        for (PriceTradeTypes tradeType : PriceTradeTypes.values()) {
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
