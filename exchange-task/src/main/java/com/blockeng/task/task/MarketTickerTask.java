package com.blockeng.task.task;

import com.blockeng.dto.TradeMarketDTO;
import com.blockeng.feign.MarketServiceClient;
import com.blockeng.framework.enums.BaseStatus;
import com.blockeng.framework.enums.DepthMergeType;
import com.blockeng.task.event.DepthEvent;
import com.blockeng.task.event.MarketEvent;
import com.blockeng.task.event.TradeEvent;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author qiang
 */
@Component
public class MarketTickerTask {

    @Autowired
    private MarketServiceClient marketServiceClient;

    @Autowired
    private RedissonClient redisson;

    private ExecutorService executor = new ThreadPoolExecutor(
            8,
            30,
            100L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(100),
            new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * 推送
     */
    @Scheduled(fixedRate = 500)
    public void pushDepths() {
        RMap<Long, TradeMarketDTO> marketMap = redisson.getMap("MARKET_CACHE");
        marketMap.values().forEach(marketDTO -> {

            //推送市场深度
            for (DepthMergeType mergeType : DepthMergeType.values()) {
                executor.execute(new DepthEvent(marketDTO, mergeType));
            }

            //推送实时成交订单数据
            executor.execute(new TradeEvent(marketDTO));
        });
    }

    /**
     * 刷新24小时成交数据
     */
    @Scheduled(fixedRate = 1000)
    public void refresh24HDeal() {
        //推送交易对信息
        executor.execute(new MarketEvent());

        marketServiceClient.tradeMarkets().forEach(market -> {
            if (market.getStatus() == BaseStatus.EFFECTIVE.getCode()) {
                marketServiceClient.refresh24hour(market.getSymbol());
            }
        });
    }
}
