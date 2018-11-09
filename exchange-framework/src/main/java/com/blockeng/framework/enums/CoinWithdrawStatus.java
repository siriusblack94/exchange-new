package com.blockeng.framework.enums;

/**
 * @Description: 提币审核状态
 * @Author: Chen Long
 * @Date: Created in 2018/5/22 上午9:58
 * @Modified by: Chen Long
 */
public enum CoinWithdrawStatus {

    PENDING(0, "审核中"),
    SUCCESS(1, "成功"),
    REFUSE(2, "拒绝"),
    CANCEL(3, "撤销"),
    PASSED(4, "审核通过"),
    SENDING(5, "打币中"),
    FAILED(6, "打币异常");

    private int code;
    private String desc;

    CoinWithdrawStatus(int code, String desc) {
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
