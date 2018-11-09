package com.blockeng.handle;

import com.blockeng.service.MarketService;
import com.blockeng.service.TurnoverOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @Description: 监听后台交易对配置修改后发送的更新缓存消息
 * @Author: Chen Long
 * @Date: Created in 2018/6/28 下午11:26
 * @Modified by: Chen Long
 */
@Component
@Slf4j
public class MarketRefreshHandle {

    @Autowired
    private MarketService marketService;

    @Autowired
    private TurnoverOrderService turnoverOrderService;

    /**
     * 刷新交易对缓存
     */
    @RabbitListener(queues = {"market.refresh"})
    public void refresh(Long marketId) {
        BigDecimal price = turnoverOrderService.queryCurrentPrice(marketId);
        marketService.refresh(marketId, price);
    }
}
