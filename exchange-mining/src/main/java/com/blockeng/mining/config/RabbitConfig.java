package com.blockeng.mining.config;


import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qiang
 */
@Configuration
public class RabbitConfig {

    @Bean
    public Queue calcAccountTxSum() {
        return new Queue("calc.account.tx.sum");
    }
}