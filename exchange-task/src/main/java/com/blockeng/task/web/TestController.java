package com.blockeng.task.web;

import com.blockeng.feign.MarketServiceClient;
import com.blockeng.feign.TradingAreaServiceClient;
import com.blockeng.task.task.MiningTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qiang
 */
@RestController
@RequestMapping("/quotes")
public class TestController {

    @Autowired
    private MarketServiceClient marketServiceClient;

    @Autowired
    private TradingAreaServiceClient tradingAreaServiceClient;

    @GetMapping("/trade_markets")
    public Object tradeMarketList() {
        return marketServiceClient.tradeMarkets();
    }

    @GetMapping("/trading_area")
    public Object tradingAreaList() {
        return tradingAreaServiceClient.tradingAreaList();
    }

    @Autowired
    private MiningTask miningTask;

    @GetMapping("/test")
    public Object test() {
        miningTask.mining();
        return "";
    }
}
