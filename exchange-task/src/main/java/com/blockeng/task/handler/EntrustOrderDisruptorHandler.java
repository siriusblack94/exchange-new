package com.blockeng.task.handler;

import com.blockeng.framework.dto.CreateKLineDTO;
import com.blockeng.framework.enums.KlineType;
import com.blockeng.task.service.TradeKLineService;
import com.lmax.disruptor.spring.boot.annotation.EventRule;
import com.lmax.disruptor.spring.boot.event.DisruptorBindEvent;
import com.lmax.disruptor.spring.boot.event.handler.DisruptorHandler;
import com.lmax.disruptor.spring.boot.event.handler.chain.HandlerChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author qiang
 */
@EventRule("/Event-Output/Generate-Output/**")
@Component
@Slf4j
public class EntrustOrderDisruptorHandler implements DisruptorHandler<DisruptorBindEvent> {

    @Autowired
    private TradeKLineService tradeKLineService;

    private ExecutorService executor = new ThreadPoolExecutor(
            12,
            100,
            100L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(100),
            new ThreadPoolExecutor.CallerRunsPolicy());

    @Override
    public void doHandler(DisruptorBindEvent event, HandlerChain<DisruptorBindEvent> handlerChain) {
        CreateKLineDTO eventSource = (CreateKLineDTO) event.getSource();

        for (KlineType klineType : KlineType.values()) {
            executor.execute(new TradeKLineService(eventSource, klineType));
            //tradeKLineService.generateKLine(eventSource, klineType);
        }
    }
}