package com.blockeng.framework.enums;

/**
 * 创新交易持仓信息
 * 持仓方向：1-买；2-卖
 *
 * @author qiang
 */
public enum PositionsDirection {

    BUY(1, "买"),
    SELL(2, "卖");

    private int value;
    private String desc;

    PositionsDirection(final int value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return this.value;
    }

    public String getDesc() {
        return this.desc;
    }

    public static PositionsDirection getByValue(int value) {
        for (PositionsDirection positionsDirection : PositionsDirection.values()) {
            if (positionsDirection.getValue() == value) {
                return positionsDirection;
            }
        }
        return null;
    }
}