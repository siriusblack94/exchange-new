package com.blockeng.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

import java.io.Serializable;

/**
 * 是否设置支付密码
 *
 * @author qiang
 */

public enum IsSetPayPassword implements IEnum {

    NO(0, "未设置"),
    YES(1, "已设置");

    private int value;
    private String desc;

    IsSetPayPassword(final int value, final String desc) {
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
