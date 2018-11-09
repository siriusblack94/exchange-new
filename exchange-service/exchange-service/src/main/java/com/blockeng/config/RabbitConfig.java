package com.blockeng.config;

import com.blockeng.enums.MessageChannel;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qiang
 */
@Configuration
public class RabbitConfig {

    @Bean
    public Queue txQueue() {
        return new Queue(MessageChannel.ORDER_TX.getChannel());
    }

    @Bean
    public Queue cancelQueue() {
        return new Queue(MessageChannel.ORDER_CANCEL.getChannel());
    }

    @Bean
    public Queue inQueue() {
        return new Queue(MessageChannel.ORDER_IN.getChannel());
    }

    @Bean
    public Queue registerRewardQueue() {
        return new Queue(MessageChannel.REGISTER_REWARD.getChannel());
    }

    @Bean
    public Queue ordeDelaNotifyQueue() {
        return new Queue(MessageChannel.ORDER_DELAY_NOTIFY.getChannel());
    }

    @Bean
    public Queue syncAccountQueue() {
        return new Queue(MessageChannel.SYNC_ACCOUNT.getChannel());
    }

    @Bean
    public Queue marketRefreshQueue() {
        return new Queue(MessageChannel.MARKET_REFRESH.getChannel());
    }

    @Bean
    public Queue bonusQueue() {
        return new Queue(MessageChannel.BONUS.getChannel());
    }

    @Bean
    public Queue smsMessageQueue() {
        return new Queue(MessageChannel.SMS_TAG.getChannel());
    }

    @Bean
    public Queue mailMessageQueue() {
        return new Queue(MessageChannel.MAIL_TAG.getChannel());
    }

    @Bean
    public Queue rechargeSuccessQueue() {
        return new Queue(MessageChannel.FINANCE_RECHARGE_SUCCESS.getChannel());
    }

    @Bean
    public Queue withdrawSuccessQueue() {
        return new Queue(MessageChannel.FINANCE_WITHDRAW_RESULT.getChannel());
    }

    @Bean
    public Queue plantUserAddress() {
        return new Queue(MessageChannel.RECHARGE_ADDRESS.getChannel());
    }

    @Bean
    public Queue poolUnlock() {
        return new Queue(MessageChannel.POOL_UNLOCK.getChannel());
    }
}