package com.blockeng.framework.enums;

/**
 * @Description: 资金流水方向
 * @Author: Chen Long
 * @Date: Created in 2018/5/13 下午2:37
 * @Modified by: Chen Long
 */
public enum AmountDirection {

    INCOME(1),  // 收入
    OUT(2);     // 支出

    private int type;

    AmountDirection(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public String toString() {
        return String.valueOf(this.type);
    }
}
