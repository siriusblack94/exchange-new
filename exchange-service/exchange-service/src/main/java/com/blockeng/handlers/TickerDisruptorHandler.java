package com.blockeng.handlers;

import com.blockeng.dto.MatchDTO;
import com.blockeng.dto.TxDTO;
import com.blockeng.handle.CacheMarketHandle;
import com.lmax.disruptor.spring.boot.annotation.EventRule;
import com.lmax.disruptor.spring.boot.event.DisruptorBindEvent;
import com.lmax.disruptor.spring.boot.event.handler.DisruptorHandler;
import com.lmax.disruptor.spring.boot.event.handler.chain.HandlerChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author qiang
 */
@EventRule("/ticker/push/**")
@Component
@Slf4j
public class TickerDisruptorHandler implements DisruptorHandler<DisruptorBindEvent> {

    @Autowired
    private CacheMarketHandle cacheMarketHandle;

    @Override
    public void doHandler(DisruptorBindEvent event, HandlerChain<DisruptorBindEvent> handlerChain) {
        try {
            TxDTO txDTO = (TxDTO) event.getSource();
            MatchDTO matchDTO = new MatchDTO()
                    .setSymbol(txDTO.getSymbol())
                    .setBuyUserId(txDTO.getBuyUserId())
                    .setSellUserId(txDTO.getSellUserId())
                    .setPrice(txDTO.getPrice());
            // 更新缓存
            cacheMarketHandle.refreshMarket(matchDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}