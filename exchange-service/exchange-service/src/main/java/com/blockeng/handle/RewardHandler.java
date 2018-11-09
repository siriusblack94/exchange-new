package com.blockeng.handle;

import com.blockeng.service.RewardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 注册奖励
 *
 * @author qiang
 */
@Component
@Slf4j
public class RewardHandler {

    @Autowired
    private RewardService rewardService;

    @RabbitListener(queues = {"register.reward"})
    public void handle(long userId) {
        try {
            rewardService.registerReward(userId);
        } catch (Exception e) {
            log.info(e.toString());
        }
    }
}
