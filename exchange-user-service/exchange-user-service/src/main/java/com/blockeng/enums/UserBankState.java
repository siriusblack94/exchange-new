package com.blockeng.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

import java.io.Serializable;

/**
 * 用户绑卡信息状态
 *
 * @author qiang
 */

public enum UserBankState implements IEnum {

    EXPIRED(0, "失效"),
    NORMAL(1, "有效");

    private int value;
    private String desc;

    UserBankState(final int value, final String desc) {
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
