package com.blockeng.wallet.ethereum.config;

import com.blockeng.wallet.enums.MessageChannel;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qiang
 */
@Configuration
public class RabbitConfig {

    @Bean
    public Queue withdrawApplyQueue() {
        return new Queue(MessageChannel.FINANCE_WITHDRAW_SEND_ETH.getName());
    }

    @Bean
    public Queue userAddress() {
        return new Queue(MessageChannel.COIN_ADDRESS_MSG.getName());
    }
}