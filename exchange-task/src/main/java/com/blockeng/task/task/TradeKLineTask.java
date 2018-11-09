package com.blockeng.task.task;

import com.blockeng.task.event.TradeKLineEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/5/15 下午4:08
 * @Modified by: Chen Long
 */
@Component
@Slf4j
public class TradeKLineTask {

    @Autowired
    private TradeKLineEvent tradeKLineEvent;

    /**
     * 币币交易生成一次K线
     */
    @Scheduled(fixedRate = 25000)
    public void generateKLine() {
        tradeKLineEvent.handle();
    }
}
