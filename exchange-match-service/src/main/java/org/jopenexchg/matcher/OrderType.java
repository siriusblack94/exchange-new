package org.jopenexchg.matcher;

/**
 * 买卖类型
 *
 * @author qiang
 */
public enum OrderType {

    BUY(1, "buy"),
    SELL(2, "sell");

    private int code;
    private String desc;

    OrderType(final int code, final String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    public static OrderType getByCode(int code) {
        for (OrderType orderType : OrderType.values()) {
            if (orderType.getCode() == code) {
                return orderType;
            }
        }
        return null;
    }
}