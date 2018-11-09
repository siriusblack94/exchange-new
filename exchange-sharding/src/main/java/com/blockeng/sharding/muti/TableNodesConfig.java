package com.blockeng.sharding.muti;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "sharding.jdbc.config.sharding")
/**
 * 读取数据源与表的路由信息
 */
@Data
public class TableNodesConfig {
    Map<String, String> tables;
}
