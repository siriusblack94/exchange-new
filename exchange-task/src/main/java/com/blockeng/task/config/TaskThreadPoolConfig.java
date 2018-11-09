package com.blockeng.task.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Descrition: 自定义线程池配置
 * @Author: Chen Long
 * @Date: Created in 2018/2/4 下午7:43
 * @Modified by:
 */
@Component
@ConfigurationProperties(prefix = "spring.task.pool") // 该注解的locations已经被启用，现在只要是在环境中，都会优先加载
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class TaskThreadPoolConfig {

    /**
     * 初始线程数量
     */
    private int corePoolSize;

    /**
     * 最大线程数量
     */
    private int maxPoolSize;

    /**
     * 队列大小
     */
    private int queueCapacity;

    /**
     * 线程空闲时接受任务等待时间
     */
    private int keepAliveSeconds;
}
