package com.blockeng.framework.enums;

/**
 * @author qiang
 */
public enum ConfigCode {

    TRADE_CONFIG_WITHDRAWALS_MIN_AMOUNT("TRADE_CONFIG_WITHDRAWALS_MIN_AMOUNT", "最小取现额（USDT）"),
    TRADE_CONFIG_WITHDRAWALS_MAX_AMOUNT("TRADE_CONFIG_WITHDRAWALS_MAX_AMOUNT", "最大取现额（USDT）"),
    TRADE_CONFIG_WITHDRAWALS_MIN_POUNDAGE("TRADE_CONFIG_WITHDRAWALS_MIN_POUNDAGE", "最小取现手续费"),
    TRADE_CONFIG_WITHDRAWALS_POUNDAGE_RATE("TRADE_CONFIG_WITHDRAWALS_POUNDAGE_RATE", "取现手续费率"),
    TRADE_CONFIG_WITHDRAWALS_BASEAMOUNT("TRADE_CONFIG_WITHDRAWALS_BASEAMOUNT", "取现基数"),
    TRADE_CONFIG_SYSTEM_STATUS("TRADE_CONFIG_SYSTEM_STATUS", "系统交易状态"),
    TRADE_CONFIG_WITHDRAWALS_STATUS("TRADE_CONFIG_WITHDRAWALS_STATUS", "提现状态"),
    TRADE_CONFIG_WITHDRAWALS_MAX_AMOUNT_PERDAY("TRADE_CONFIG_WITHDRAWALS_MAX_AMOUNT_PERDAY", "每日最大提现额（USDT）"),
    TRADE_CONFIG_DATA_SYNC_STATUS("TRADE_CONFIG_DATA_SYNC_STATUS", "交易数据同步状态"),
    TRADE_CONFIG_CNY2USDT("TRADE_CONFIG_CNY2USDT", "人民币充值USDT换算费率"),
    TRADE_CONFIG_USDT2CNY("TRADE_CONFIG_USDT2CNY", "人民币提现USDT换算费率");

    private String value;

    private String desc;

    public String getDesc() {
        return desc;
    }

    ConfigCode(final String value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return this.value;
    }
}