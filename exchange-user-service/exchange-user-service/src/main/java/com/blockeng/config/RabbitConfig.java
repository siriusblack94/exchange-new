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
    public Queue syncAccountQueue() {
        return new Queue("sync.account");
    }
}