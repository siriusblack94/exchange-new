package com.blockeng.task.task;

import com.blockeng.feign.MarketServiceClient;
import com.blockeng.framework.constants.Constant;
import com.blockeng.framework.enums.BaseStatus;
import com.blockeng.task.event.KlineEvent;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class TopicKLineTask implements Constant {

    @Autowired
    private MarketServiceClient marketServiceClient;

    private ExecutorService executor = new ThreadPoolExecutor(
            5,
            10,
            100L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(30),
            new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * 每3秒推送K线数据
     */
    @Scheduled(fixedRate = 3000)
    public void pushKline() {
        String channel = "market.%s.kline.%s";
        marketServiceClient.tradeMarkets().forEach(market -> {
            if (market.getStatus() == BaseStatus.EFFECTIVE.getCode()) {
                executor.execute(new KlineEvent(market.getSymbol(), channel, REDIS_KEY_TRADE_KLINE));
            }
        });
    }
}