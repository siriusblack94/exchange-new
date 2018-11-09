package com.blockeng.mining;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/6/29 下午11:57
 * @Modified by: Chen Long
 */

@SpringBootApplication(scanBasePackages = {"com.blockeng.mining"})
public class InitDataApplication {
    public static void main(String[] args) {
        SpringApplication.run(InitDataApplication.class, args);
    }
}
