package com.blockeng.enums;

/**
 * @Description: 消息通道
 * @Author: Chen Long
 * @Date: Created in 2018/6/28 下午11:40
 * @Modified by: Chen Long
 */
public enum MessageChannel {


    ORDER_TX("order.tx"),
    ORDER_CANCEL("order.cancel"),
    ORDER_IN("order.in"),
    ORDER_DELAY_NOTIFY("order.delay.notify"),
    SYNC_ACCOUNT("sync.account"),
    MARKET_REFRESH("market.refresh"),
    BONUS("bonus"),
    REGISTER_REWARD("register.reward"),
    FINANCE_RECHARGE_SUCCESS("finance.recharge.success"),
    FINANCE_WITHDRAW_RESULT("finance.withdraw.result"),
    RECHARGE_ADDRESS("plant.user.address"),
    POOL_UNLOCK("pool.unlock"),
    SMS_TAG("touser.message.sms"),
    MAIL_TAG("touser.message.mail");

    private String channel;

    MessageChannel(String channel) {
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
