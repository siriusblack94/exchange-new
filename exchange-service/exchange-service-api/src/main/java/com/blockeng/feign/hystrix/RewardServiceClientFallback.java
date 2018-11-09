package com.blockeng.feign.hystrix;

import com.blockeng.feign.RewardServiceClient;
import org.springframework.stereotype.Component;

/**
 * @Description: 奖励（注册奖励、邀请奖励服务）降级服务
 * @Author: Chen Long
 * @Date: Created in 2018/5/25 下午4:57
 * @Modified by: Chen Long
 */
@Component
public class RewardServiceClientFallback implements RewardServiceClient {

    /**
     * 注册奖励
     *
     * @param userId 用户ID
     * @return
     */
    @Override
    public boolean registerReward(Long userId) {
        return false;
    }

    /**
     * 邀请奖励
     *
     * @param userId 用户ID
     * @return
     */
    @Override
    public boolean inviteReward(Long userId) {
        return false;
    }

    @Override
    public boolean rechargeReward(Long userId) {
        return false;
    }
}
