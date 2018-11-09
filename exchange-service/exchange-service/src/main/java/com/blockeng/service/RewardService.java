package com.blockeng.service;

/**
 * @Description: 奖励（注册奖励、邀请奖励）
 * @Author: Chen Long
 * @Date: Created in 2018/5/25 下午4:47
 * @Modified by: shaodw
 */
public interface RewardService {

    /**
     * 注册奖励
     *
     * @param userId 用户ID
     */
    boolean registerReward(Long userId);

    /**
     * 邀请奖励
     *
     * @param userId 用户ID
     */
    boolean inviteReward(Long userId);


    /**
     * 首次充值奖励
     *
     * @param userId 用户ID
     * @return
     */
    boolean rechargeReward(Long userId);
}
