package com.blockeng.mining;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/6/29 下午11:57
 * @Modified by: Chen Long
 */
@EnableTransactionManagement
@SpringBootApplication(
        scanBasePackages = {
                "com.blockeng"
        }
)
@EnableDiscoveryClient
@EnableFeignClients(
        basePackages = "com.blockeng"
)
@EnableAsync
@EnableScheduling
public class MiningApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiningApplication.class, args);
    }
}
