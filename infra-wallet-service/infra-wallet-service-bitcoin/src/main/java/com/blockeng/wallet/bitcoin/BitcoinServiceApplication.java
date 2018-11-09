package com.blockeng.wallet.bitcoin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = "com.blockeng"
)
public class BitcoinServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BitcoinServiceApplication.class, args);
    }

}
