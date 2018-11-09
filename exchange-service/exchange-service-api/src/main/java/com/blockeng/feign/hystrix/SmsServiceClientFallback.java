package com.blockeng.feign.hystrix;

import com.blockeng.dto.SendForm;
import com.blockeng.feign.SmsServiceClient;
import org.springframework.stereotype.Component;

/**
 * @author qiang
 */
@Component
public class SmsServiceClientFallback implements SmsServiceClient {

    @Override
    public boolean sendTo(SendForm sendForm) {
        return  true;
    }
}
