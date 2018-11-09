package com.blockeng.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

import java.io.Serializable;

/**
 * 证件类型
 *
 * @author qiang
 */

public enum IdCardType implements IEnum {

    IDCARD(1, "身份证"),
    MILITARY_OFFICER(2, "军官证"),
    PASSPORT(3, "护照"),
    TAIWA_RESIDENT_PASS(4, "台湾居民通行证"),
    HONG_KONG_AND_MACAU_RESIDENTS_PASS(5, "港澳居民通行证"),
    OTHER(9, "其他");

    private int value;
    private String desc;

    IdCardType(final int value, final String desc) {
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
