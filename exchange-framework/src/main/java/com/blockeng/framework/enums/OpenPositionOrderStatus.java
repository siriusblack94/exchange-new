package com.blockeng.framework.enums;

/**
 * @Description: 持仓订单状态
 * @Author: Chen Long
 * @Date: Created in 2018/4/20 下午12:57
 * @Modified by: Chen Long
 */
public enum OpenPositionOrderStatus {

    OPEN(1, "未平仓"), CLOSE(2, "已平仓");

    private int code;
    private String desc;

    OpenPositionOrderStatus(int code, String desc) {
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
