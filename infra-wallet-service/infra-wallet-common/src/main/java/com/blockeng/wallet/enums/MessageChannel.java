package com.blockeng.wallet.enums;

/**
 * 消息通道名称
 */
public enum MessageChannel {

    COIN_RECHARGE_MSG("finance.recharge.success"),
    COIN_WITHDRAW_MSG("finance.withdraw.result"),
    FINANCE_WITHDRAW_SEND_ACT("finance.withdraw.send.act"),
    FINANCE_WITHDRAW_SEND_BTC("finance.withdraw.send.btc"),
    FINANCE_WITHDRAW_SEND_ETH("finance.withdraw.send.eth"),
    FINANCE_WITHDRAW_SEND_ETC("finance.withdraw.send.etc"),
    FINANCE_WITHDRAW_SEND_NEO("finance.withdraw.send.neo"),
    FINANCE_WITHDRAW_SEND_WCG("finance.withdraw.send.wcg"),
    FINANCE_WITHDRAW_SEND_XRP("finance.withdraw.send.xrp"),
    COIN_ADDRESS_MSG("plant.user.address");

    /**
     * 消息通道名称
     */
    private String name;

    MessageChannel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
