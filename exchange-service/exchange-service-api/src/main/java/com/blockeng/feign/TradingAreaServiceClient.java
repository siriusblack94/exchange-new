package com.blockeng.feign;

import com.blockeng.dto.TradeAreaDTO;
import com.blockeng.feign.hystrix.TradingAreaServiceClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author qiang
 */
@FeignClient(value = "exchange-service", fallback = TradingAreaServiceClientFallback.class)
public interface TradingAreaServiceClient {

    @RequestMapping(value = "/trading_area/list", method = RequestMethod.GET)
    List<TradeAreaDTO> tradingAreaList();
}