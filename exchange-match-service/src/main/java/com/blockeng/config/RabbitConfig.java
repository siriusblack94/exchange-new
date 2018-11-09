package com.blockeng.config;

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
        return new Queue("order.tx");
    }

    @Bean
    public Queue inQueue() {
        return new Queue("order.in");
    }

    @Bean
    public Queue ordeDelaNotifyQueue() {
        return new Queue("order.delay.notify");
    }

    @Bean
    public Queue marketDataKlineQueue() {
        return new Queue("marketdata.kline");
    }
}