package com.blockeng.boss.config;

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
}