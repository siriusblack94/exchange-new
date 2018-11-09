package com.blockeng.feign;

import com.blockeng.dto.CoinTransferForm;
import com.blockeng.feign.hystrix.CoinTransferServiceClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author: jakiro
 * @Date: 2018-11-02 10:22
 * @Description:
 */
@FeignClient(value = "exchange-service", fallback = CoinTransferServiceClientFallback.class)
public interface CoinTransferServiceClient {

    @RequestMapping(value = "/coinTransfer/doTransfer", method = RequestMethod.POST)
    boolean doTransfer(@RequestBody CoinTransferForm coinTransferForm);
}
