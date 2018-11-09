package com.blockeng.framework.enums;


/**
 * @Description: 资金流水业务类型
 * @Author: Chen Long
 * @Date: Created in 2018/5/13 下午2:37
 * @Modified by: Chen Long
 */
public enum BusinessType {
    POINTS_UNLOCK(  "points_unlock","积分兑换解冻"),
    BUCKLE_LOCK("buckle_lock","资金补扣冻结"),
    BUCKLE_UNLOCK("buckle_unlock","资金补扣解冻"),
    BUCKLE("buckle_done","资金补扣"),
    ONCE_RELEASE("private_once_release", "私募一次性释放"),
    RELEASE("private_release", "私募释放"),
    RECHARGE("recharge", "充值"),
    RECHARGE_REWARD("recharge_reward", "首次充值奖励"),
    WITHDRAW("withdraw", "提现"),
    WITHDRAW_FEE("withdraw_fee", "提现"),
    TRADE_CREATE("trade_create", "币币交易委托下单"),
    TRADE_CANCEL("trade_cancel", "币币交撤销委托"),
    TRADE_DEAL_ERROR("trade_deal_error", "币币交易撮合异常，解冻"),
    TRADE_DEAL("trade_deal", "币币交易撮合成交"),
    TRADE_DEAL_FEE("trade_deal_fee", "币币交易撮合成交手续费"),
    REGISTER_REWARD("register_reward", "注册奖励"),
    INVITE_REWARD("invite_reward", "邀请奖励"),
    TRADING_DIG("trading_dig", "交易挖矿"),
    POOL_DIG("pool_dig", "矿池奖励"),
    MINE_DIG("mine_dig", "挖矿分红"),
    PLANT_COIN_DIG("plant_coin_dig", "持有平台币分红"),
    DIVIDEND_DIG("dividend_dig", "每周邀请奖励"),
    BONUS("bonus", "分红"),
    BONUS_UNLOCK("invite_reward_unlock", "邀请奖励解冻"),
    COIN_TRANSFER("coin_transfer","站内转帐");

    /**
     *
     */
    private String code;

    /**
     * 标识
     */
    private String desc;

    BusinessType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(String code) {
        for (BusinessType type : BusinessType.values()) {
            if (type.getCode().equals(code)) {
                return type.getDesc();
            }
        }
        return null;
    }

    public static BusinessType getEnumByCode(String code){

        for (BusinessType type : BusinessType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}