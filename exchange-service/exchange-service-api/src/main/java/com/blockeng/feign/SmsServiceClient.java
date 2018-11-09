package com.blockeng.feign;

import com.blockeng.dto.SendForm;
import com.blockeng.feign.hystrix.SmsServiceClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author qiang
 */
@FeignClient(value = "exchange-service", fallback = SmsServiceClientFallback.class)
public interface SmsServiceClient {

    @RequestMapping(value = "/sms/sendTo", method = RequestMethod.POST)
    boolean sendTo(@RequestBody SendForm sendForm);
}