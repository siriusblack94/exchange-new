package com.blockeng.task.event;

import com.blockeng.dto.MarketDTO;
import com.blockeng.feign.MarketServiceClient;
import com.blockeng.framework.dto.CreateKLineDTO;
import com.lmax.disruptor.spring.boot.DisruptorTemplate;
import com.lmax.disruptor.spring.boot.event.DisruptorBindEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author qiang
 */
@Component
@Slf4j
public class TradeKLineEvent {

    @Autowired
    private MarketServiceClient marketServiceClient;

    @Autowired
    private DisruptorTemplate disruptorTemplate;

    public void handle() {
        List<MarketDTO> markets = marketServiceClient.tradeMarkets();
        if (!CollectionUtils.isEmpty(markets)) {
            for (MarketDTO market : markets) {
                CreateKLineDTO createKLineDTO = new CreateKLineDTO()
                        .setSymbol(market.getSymbol())
                        .setPrice(market.getOpenPrice())
                        .setVolume(BigDecimal.ZERO);
                DisruptorBindEvent event = new DisruptorBindEvent(createKLineDTO, "message " + Math.random());
                event.setEvent("Event-Output");
                event.setTag("Generate-Output");
                event.setKey("id-" + Math.random());
                disruptorTemplate.publishEvent(event);
            }
        }
    }
}
