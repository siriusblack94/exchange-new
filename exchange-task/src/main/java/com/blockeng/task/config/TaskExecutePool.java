package com.blockeng.task.config;

import com.blockeng.framework.config.ExecutePoll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Descrition:
 * @Author: Chen Long
 * @Date: Created in 2018/2/4 下午7:49
 * @Modified by:
 */
@Configuration
@EnableAsync
public class TaskExecutePool extends ExecutePoll {

    @Autowired
    private TaskThreadPoolConfig config;

    /**
     * 根据交易对生成K线
     *
     * @return
     */
    @Bean
    public Executor tradeKLineExecutor() {
        return super.getTaskExecutor("TradeKLineExecutor-",
                config.getCorePoolSize(),
                config.getMaxPoolSize(),
                config.getQueueCapacity(),
                config.getKeepAliveSeconds());
    }

    /**
     * 根据K线类型生成K线
     *
     * @return
     */
    @Bean
    public Executor lineTypeTaskAsyncPool() {
        return super.getTaskExecutor("LineTypeTaskAsyncPool-",
                config.getCorePoolSize(),
                config.getMaxPoolSize(),
                config.getQueueCapacity(),
                config.getKeepAliveSeconds());
    }
}
