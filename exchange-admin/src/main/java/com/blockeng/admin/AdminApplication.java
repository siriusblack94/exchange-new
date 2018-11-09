package com.blockeng.admin;

import com.blockeng.sharding.EnableExtSharding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.jdbc.DataSourceHealthIndicatorAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author qiang
 */
@SpringBootApplication(exclude = {
        DataSourceHealthIndicatorAutoConfiguration.class
}, scanBasePackages = {"com.blockeng.admin"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {
        "com.blockeng"
})
@EnableAsync
@RefreshScope
@EnableExtSharding
@EnableMongoRepositories(basePackages = "com.blockeng.repository")
public class AdminApplication {
    public static void main(String[] args) {

        SpringApplication.run(AdminApplication.class, args);
    }
}