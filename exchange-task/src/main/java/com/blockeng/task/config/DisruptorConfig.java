package com.blockeng.task.config;

import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.spring.boot.util.WaitStrategys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author maple
 * @date 2018/10/14 18:05
 **/
@Configuration
public class DisruptorConfig {
    @Bean
    public WaitStrategy waitStrategy() {
        return WaitStrategys.BLOCKING_WAIT;
    }
}
