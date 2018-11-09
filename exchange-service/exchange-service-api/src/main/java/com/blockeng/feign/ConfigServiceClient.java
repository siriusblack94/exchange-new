package com.blockeng.feign;

import com.blockeng.dto.ConfigDTO;
import com.blockeng.feign.hystrix.ConfigServiceClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "exchange-service", fallback = ConfigServiceClientFallback.class)
public interface ConfigServiceClient {

    @GetMapping("/config/getConfig")
    ConfigDTO getConfig(@RequestParam("type") String type, @RequestParam("code") String code);
}
