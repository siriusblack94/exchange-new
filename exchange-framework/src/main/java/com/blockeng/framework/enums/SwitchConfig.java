package com.blockeng.framework.enums;

/**
 * @Description: 开关配置
 * @Author: Chen Long
 * @Date: Created in 2018/5/25 下午5:15
 * @Modified by: Chen Long
 */
public enum SwitchConfig {

    ON("1", "开"),
    OFF("0", "关");

    private String code;
    private String desc;

    SwitchConfig(String code, String desc) {
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
