package com.blockeng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author qiang
 */
@SpringBootApplication(exclude = {
        SecurityAutoConfiguration.class
})
@EnableDiscoveryClient
@EnableFeignClients
@RefreshScope
public class ChanApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChanApplication.class, args);
    }
}