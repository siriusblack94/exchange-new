package com.blockeng.config;

import com.blockeng.framework.enums.MarketType;
import com.blockeng.service.MarketService;
import com.blockeng.service.TurnoverOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/5/29 下午6:37
 * @Modified by: Chen Long
 */
@Component
@Slf4j
public class
InitRunner implements CommandLineRunner {

    @Autowired
    private MarketService marketService;

    @Autowired
    private TurnoverOrderService turnoverOrderService;

    @Override
    public void run(String... args) throws Exception {
        try {
            log.info("缓存数据初始化开始");
            long start = System.currentTimeMillis();
            marketService.queryMarkets().forEach(market -> {
                // 币币交易行情初始化
                // 最新成交价
                BigDecimal price = turnoverOrderService.queryCurrentPrice(market.getId());
                marketService.refresh(market.getId(), price);
            });
            long end = System.currentTimeMillis();
            log.info("缓存数据初始化完成，耗时：{} ms", (end - start));
        } catch (Exception e) {
            e.printStackTrace();
            log.info("初始化出错");
        }
    }
}
