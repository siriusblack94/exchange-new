package com.blockeng.framework.enums;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/4/18 上午11:39
 * @Modified by: Chen Long
 */
public enum OrderStatus {

    PENDING(0, "待成交"),
    DEAL(1, "已成交"),
    CANCEL(2, "撤销"),
    MATCH_ABNORMAL(3, "异常");

    /**
     * 代码
     */
    private int code;

    /**
     * 描述
     */
    private String desc;

    OrderStatus(int code, String desc) {
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
