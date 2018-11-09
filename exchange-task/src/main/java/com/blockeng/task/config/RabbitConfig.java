package com.blockeng.task.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qiang
 */
@Configuration
public class RabbitConfig {

    @Bean
    public Queue bonusQueue() {
        return new Queue("bonus");
    }

    @Bean
    public Queue marketDataKlineQueue() {
        return new Queue("marketdata.kline");
    }
}