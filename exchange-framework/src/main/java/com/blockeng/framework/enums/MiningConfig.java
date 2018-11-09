package com.blockeng.framework.enums;

/**
 * 挖矿配置
 */
public enum MiningConfig {

    TYPE("MINING"),
    CODE_SWITCH("SWITCH"),
    CODE_COIN_ID("COIN_ID"),
    CODE_COIN_NAME("COIN_NAME"),
    OPEN("1");

    private String value;

    MiningConfig(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
