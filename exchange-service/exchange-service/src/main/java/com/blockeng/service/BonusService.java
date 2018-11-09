package com.blockeng.service;

/**
 * @author qiang
 */
public interface BonusService {

    /**
     * 交易挖矿返还
     *
     * @param date
     */
    public void tradingDigSet(String date);

    /**
     * 分红返还
     */
    public void bonusSet(String date);

    /**
     * 邀请奖励返还
     */
    public void inviteRewardsSet(String date);
}
