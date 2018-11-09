package com.blockeng.framework.enums;

public enum CashRechargeStatus {

    PENDING(0, "待审核"), PASSED(1, "审核通过"), REFUSE(2, "拒绝"), SUCCESS(3, "成功");

    private int code;
    private String desc;

    CashRechargeStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

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

    public static CashRechargeStatus getByCode(int code) {
        for (CashRechargeStatus status : CashRechargeStatus.values()) {
            if (code == status.getCode()) {
                return status;
            }
        }
        return null;
    }
}
