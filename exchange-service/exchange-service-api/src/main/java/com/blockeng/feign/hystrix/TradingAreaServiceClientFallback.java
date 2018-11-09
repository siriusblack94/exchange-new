package com.blockeng.feign.hystrix;

import com.blockeng.dto.TradeAreaDTO;
import com.blockeng.feign.TradingAreaServiceClient;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author qiang
 */
@Component
public class TradingAreaServiceClientFallback implements TradingAreaServiceClient {

    @Override
    public List<TradeAreaDTO> tradingAreaList() {
        return Collections.emptyList();
    }
}
