package com.blockeng.framework.enums;

/**
 * @Description: 基础状态
 * @Author: Chen Long
 * @Date: Created in 2018/5/13 下午2:50
 * @Modified by: Chen Long
 */
public enum BaseStatus {

    EFFECTIVE(1, "有效"),
    INVALID(0, "无效");

    /**
     * 代码
     */
    private int code;

    /**
     * 描述
     */
    private String desc;

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

    BaseStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
