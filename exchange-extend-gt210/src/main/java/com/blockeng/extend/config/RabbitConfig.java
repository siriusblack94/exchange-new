package com.blockeng.extend.config;


import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qiang
 */
@Configuration
public class RabbitConfig {

    @Bean
    public Queue ResgisterSyn() {
        return new Queue("user.info.syn");
    }

    @Bean
    public Queue userUpdateSyn() {
        return new Queue("user.info.update.syn");
    }
}