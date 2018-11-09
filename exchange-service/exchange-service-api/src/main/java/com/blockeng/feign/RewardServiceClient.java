package com.blockeng.feign;

import com.blockeng.feign.hystrix.RewardServiceClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Description: 奖励（注册、邀请、充值奖励）
 * @Author: Chen Long
 * @Date: Created in 2018/5/25 下午4:56
 * @Modified by: Chen Long
 */
@FeignClient(value = "exchange-service", fallback = RewardServiceClientFallback.class)
public interface RewardServiceClient {

    /**
     * 注册奖励
     *
     * @param userId 用户ID
     */
    @RequestMapping(value = "/reward/register/{userId}", method = RequestMethod.POST)
    boolean registerReward(@PathVariable("userId") Long userId);

    /**
     * 邀请奖励
     *
     * @param userId 用户ID
     */
    @RequestMapping(value = "/reward/invite/{userId}", method = RequestMethod.POST)
    boolean inviteReward(@PathVariable("userId") Long userId);

    /**
     * 首次充值奖励
     *
     * @param userId 用户ID
     */
    @PostMapping("/reward/recharge/{userId}")
    boolean rechargeReward(@PathVariable("userId") Long userId);
}
