package com.blockeng.boot;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author maple
 * @date 2018/10/25 11:17
 * 该配置用于让读取数据库的操作在mq监听实例化之前
 **/
@Configuration
@AutoConfigureBefore(RabbitAutoConfiguration.class)
public class LoadOrderDataConfig {

    @Bean
    public LoadOrderDataRunner loadOrderDataRunner() {
        return new LoadOrderDataRunner();
    }
}
