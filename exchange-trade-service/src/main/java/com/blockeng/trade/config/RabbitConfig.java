package com.blockeng.trade.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: RabbitMQ 消息队列
 * @Author: Chen Long
 * @Date: Created in 2018/7/18 下午10:22
 * @Modified by: Chen Long
 */
@Configuration
public class RabbitConfig {


    /**
     * 撤单消息队列
     *
     * @return
     */
    @Bean
    public Queue cancelQueue() {
        return new Queue("order.cancel");
    }


}