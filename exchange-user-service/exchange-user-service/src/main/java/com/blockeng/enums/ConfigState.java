package com.blockeng.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

import java.io.Serializable;

/**
 * 配置类型
 *
 * @author qiang
 */

public enum ConfigState implements IEnum {

    DISABLED(0, "禁用"),
    ENABLED(1, "启用");

    private int value;
    private String desc;

    ConfigState(final int value, final String desc) {
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
