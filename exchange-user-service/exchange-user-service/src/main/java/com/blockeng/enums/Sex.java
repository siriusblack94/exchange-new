package com.blockeng.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

import java.io.Serializable;

/**
 * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
 *
 * @author qiang
 */

public enum Sex implements IEnum {

    MALE(1, "男性"),
    FEMALE(2, "女性"),
    UNKNOWN(0, "未知");

    private int value;
    private String desc;

    Sex(final int value, final String desc) {
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
