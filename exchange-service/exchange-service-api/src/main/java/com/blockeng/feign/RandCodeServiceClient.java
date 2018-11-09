package com.blockeng.feign;

import com.blockeng.dto.RandCodeVerifyDTO;
import com.blockeng.feign.hystrix.RandCodeServiceClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author qiang
 */
@FeignClient(value = "exchange-service", fallback = RandCodeServiceClientFallback.class)
public interface RandCodeServiceClient {

    @RequestMapping(value = "/api/v1/dm/verify", method = RequestMethod.POST)
    boolean verify(@RequestBody RandCodeVerifyDTO randCodeVerifyDTO);
}