package com.blockeng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class}
,scanBasePackages = {"com.blockeng"})
@EnableDiscoveryClient
@RefreshScope
public class MailtestApplication {

    public static void main(String[] args) {
        SpringApplication.run(MailtestApplication.class, args);
    }
}
