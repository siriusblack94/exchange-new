package com.blockeng.framework.enums;

/**
 * @Description: 钱包类型
 * @Author: Chen Long
 * @Date: Created in 2018/5/21 下午6:56
 * @Modified by: Chen Long
 */
public enum WalletType {

    QBB("qbb", "钱包币"),
    RGB("rgb", "认购币");

    private String code;
    private String desc;

    WalletType(String code, String desc) {
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
