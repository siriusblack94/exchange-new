package com.blockeng.framework.enums;

/**
 * @Description: 价格类型
 * @Author: Chen Long
 * @Date: Created in 2018/4/17 下午3:41
 * @Modified by: Chen Long
 */
public enum PriceType {

    CURRENT_PRICE(1, "市价"), LIMIT_PRICE(2, "限价");

    private int code;
    private String name;

    PriceType(int code, String name) {
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

    public static PriceType getByCode(int code) {
        for (PriceType priceType : PriceType.values()) {
            if (priceType.getCode() == code) {
                return priceType;
            }
        }
        return null;
    }
}
