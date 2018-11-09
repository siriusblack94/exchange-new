package com.blockeng.admin.enums;

public enum FinanceDetailRemark {

    TRADE_DETAIL_REMARK_TYPE_RECHARGE("recharge"),
    TRADE_DETAIL_REMARK_TYPE_RECHARGE_INTO("recharge_into"),
    TRADE_DETAIL_REMARK_TYPE_BONUS_INFO("bonus_into"),
    TRADE_DETAIL_REMARK_TYPE_BONUS_FREEZE("bonus_freeze"),
    TRADE_DETAIL_REMARK_TYPE_WITHDRAWALS("withdrawals"),
    TRADE_DETAIL_REMARK_TYPE_WITHDRAWALS_OUT("withdrawals_out"),
    TRADE_DETAIL_REMARK_TYPE_WITHDRAWALS_POUNDAGE("withdrawals_poundage"),
    TRADE_DETAIL_REMARK_TYPE_ORDER_CREATE("order_create"),
    TRADE_DETAIL_REMARK_TYPE_ORDER_TURNOVER("order_turnover"),
    TRADE_DETAIL_REMARK_TYPE_ORDER_TURNOVER_POUNDAGE("order_turnover_poundage"),
    TRADE_DETAIL_REMARK_TYPE_ORDER_CANNCEL("order_cancel"),
    TRADE_DETAIL_REMARK_TYPE_ORDER_EXCEPTION("order_exception"),
    TRADE_DETAIL_REMARK_TYPE_BONUS_REGISTER("bonus_register"),
    TRADE_DETAIL_REMARK_TYPE_BONUS_DEAL("bonus_deal"),
    TRADE_DETAIL_REMARK_TYPE_CNY_BTCX_EXCHANGE("cny_btcx_exchange"),
    TRADE_DETAIL_REMARK_TYPE_BONUS_UNLOCKED("bonus_unlocked"),
    TRADE_DETAIL_REMARK_TYPE_INVITE_REWARD("invite_reward"),
    TRADE_DETAIL_REMARK_TYPE_REGISTER_REWARD("register_reward");


    private String remark;

    FinanceDetailRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }

    public String toString() {
        return this.remark;
    }
}
