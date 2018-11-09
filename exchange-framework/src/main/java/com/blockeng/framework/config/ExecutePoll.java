package com.blockeng.framework.config;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/5/15 下午5:54
 * @Modified by: Chen Long
 */
public class ExecutePoll {

    /**
     * 创建自定义线程池
     *
     * @param name
     * @param coreSize
     * @param maxSize
     * @param queueCapacity
     * @param keepAliveSeconds
     * @return
     */
    public Executor getTaskExecutor(String name, int coreSize, int maxSize, int queueCapacity, int keepAliveSeconds) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(coreSize);
        executor.setMaxPoolSize(maxSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadNamePrefix(name);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
