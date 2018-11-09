package com.blockeng.framework.enums;

/**
 * 矿池状态
 */
public enum MinePoolStatus {

    PENDING(0, "待审核"),
    EFFECTIVE(1, "已生效"),
    REFUSE(2, "拒绝");

    private int code;
    private String desc;

    MinePoolStatus(int code, String desc) {
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
