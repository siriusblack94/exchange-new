package com.blockeng.framework.enums;

/**
 * @Description: 交易市场类型
 * @Author: Chen Long
 * @Date: Created in 2018/5/13 下午2:09
 * @Modified by: Chen Long
 */
public enum MarketType {

    TRADE(1, "币币交易");

    /**
     * 代码
     */
    private int code;

    /**
     * 描述
     */
    private String desc;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    MarketType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
