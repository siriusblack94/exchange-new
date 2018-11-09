package com.blockeng.feign.hystrix;

import com.blockeng.feign.IP2regionServiceClient;
import org.springframework.stereotype.Component;

/**
 * @author qiang
 */
@Component
public class IP2regionServiceClientCallback implements IP2regionServiceClient {
    @Override
    public String getIpInfo(String ip) {
        return null;
    }
}
