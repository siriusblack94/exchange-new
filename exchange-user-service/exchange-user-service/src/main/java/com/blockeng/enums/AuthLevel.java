package com.blockeng.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

import java.io.Serializable;

/**
 * @author qiang
 */

public enum AuthLevel implements IEnum {

    UNAUTHERIZED(0, "未认证"),
    SIMPLE_AUTHERIZED(1, "初级认证"),
    ADVANCED_AUTHERIZED(2, "高级认证");

    private int value;
    private String desc;

    AuthLevel(final int value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Serializable getValue() {
        return this.value;
    }

    public String getDesc() {
        return this.desc;
    }
}
