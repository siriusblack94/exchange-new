package com.blockeng.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

import java.io.Serializable;

/**
 * 用户状态
 *
 * @author qiang
 */

public enum UserState implements IEnum {

    LOCK(0, "锁定"),
    NORMAL(1, "有效");

    private int value;
    private String desc;

    UserState(final int value, final String desc) {
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
