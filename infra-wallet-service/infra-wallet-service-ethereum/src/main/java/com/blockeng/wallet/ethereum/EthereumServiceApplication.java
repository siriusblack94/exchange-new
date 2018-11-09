package com.blockeng.wallet.ethereum;

import com.clg.wallet.bean.ClientBean;
import com.clg.wallet.newclient.ClientFactory;
import com.clg.wallet.newclient.EthNewClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.math.BigDecimal;

@SpringBootApplication(
        scanBasePackages = "com.blockeng"
)
public class EthereumServiceApplication {

    public static void main111(String[] args) {
        SpringApplication.run(EthereumServiceApplication.class, args);
    }



}
