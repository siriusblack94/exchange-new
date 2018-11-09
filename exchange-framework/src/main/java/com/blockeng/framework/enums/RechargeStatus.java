package com.blockeng.framework.enums;

/**
 * @Description: 充值状态
 * @Author: Chen Long
 * @Date: Created in 2018/5/21 下午8:57
 * @Modified by: Chen Long
 */
public enum RechargeStatus {

    PENDING(0, "待入账"),
    RECHARGE_FAILED(1, "充值失败"),
    FAILED(2, "到账失败"),
    SUCCESS(3, "到账成功");

    /**
     * 代码
     */
    private int code;

    /**
     * 描述
     */
    private String desc;

    RechargeStatus(int code, String desc) {
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
}
