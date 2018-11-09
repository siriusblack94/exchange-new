package com.blockeng.admin.enums;

/**
 * @Description: 消息通道
 * @Author: Chen Long
 * @Date: Created in 2018/6/28 下午11:40
 * @Modified by: Chen Long
 */
public enum MessageChannel {

    REFRESH_MARKET("market.refresh"),
    FINANCE_WITHDRAW_RESULT("finance.withdraw.result");


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
