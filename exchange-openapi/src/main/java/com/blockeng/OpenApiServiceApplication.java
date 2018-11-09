package com.blockeng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
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
public class OpenApiServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenApiServiceApplication.class, args);
    }
}
