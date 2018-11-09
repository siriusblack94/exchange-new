package com.blockeng.framework.enums;

/**
 * 审核权限
 */
public enum PrivilegeType {

    CASH_RECHARGE_AUDIT("cash_recharge_audit", "法币充值审核"),
    CASH_WITHDRAW_AUDIT("cash_withdraw_audit", "法币提现审核"),
    COIN_WITHDRAW_AUDIT("coin_withdraw_audit", "数字货币提现审核"),
    COIN_BUCKLE_AUDIT("coin_buckle_audit", "补扣审核");

    private String code;
    private String desc;

    PrivilegeType(String code, String desc) {
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
