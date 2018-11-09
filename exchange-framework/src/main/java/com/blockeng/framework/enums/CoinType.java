package com.blockeng.framework.enums;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/5/21 下午6:56
 * @Modified by: Chen Long
 */
public enum CoinType {

    BTC("default", "比特币系列"),
    ETH("eth", "以太坊"),
    ETH_TOKEN("ethToken", "以太坊代币");

    /**
     * 代码
     */
    private String code;

    /**
     * 描述
     */
    private String desc;

    CoinType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
