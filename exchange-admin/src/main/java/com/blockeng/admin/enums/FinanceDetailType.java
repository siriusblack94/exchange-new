package com.blockeng.admin.enums;

public enum FinanceDetailType {

    TRADE_DETAIL_TYPE_INCOME(1),
    TRADE_DETAIL_TYPE_PAYOUT(2);

    private int type;

    private FinanceDetailType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public String toString() {
        return String.valueOf(this.type);
    }
}
