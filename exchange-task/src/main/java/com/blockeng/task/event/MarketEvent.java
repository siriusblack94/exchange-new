package com.blockeng.task.event;

import com.blockeng.dto.TradeAreaDTO;
import com.blockeng.dto.TradeMarketDTO;
import com.blockeng.feign.MarketServiceClient;
import com.blockeng.feign.TradingAreaServiceClient;
import com.blockeng.framework.constants.Constant;
import com.blockeng.framework.dto.MessagePayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qiang
 */
@Component
@Slf4j
public class MarketEvent implements Runnable {

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        MarketEvent.objectMapper = objectMapper;
    }

    @Autowired
    public void setMarketServiceClient(MarketServiceClient marketServiceClient) {
        MarketEvent.marketServiceClient = marketServiceClient;
    }

    @Autowired
    public void setTradingAreaServiceClient(TradingAreaServiceClient tradingAreaServiceClient) {
        MarketEvent.tradingAreaServiceClient = tradingAreaServiceClient;
    }

    @Autowired
    public void setKafkaTemplate(KafkaTemplate kafkaTemplate) {
        MarketEvent.kafkaTemplate = kafkaTemplate;
    }

    @Autowired
    public void setRedisson(RedissonClient redisson) {
        MarketEvent.redisson = redisson;
    }

    private static ObjectMapper objectMapper;

    private static MarketServiceClient marketServiceClient;

    private static TradingAreaServiceClient tradingAreaServiceClient;

    private static KafkaTemplate kafkaTemplate;

    private static RedissonClient redisson;

    @Override
    public void run() {
        // 推送币币交易对信息
        List<TradeAreaDTO> tradeAreaList = tradingAreaServiceClient.tradingAreaList();
        tradeAreaList.forEach(tradeArea -> {
            String groupName = String.format("market.%s.ticker", tradeArea.getCode()).toLowerCase();
            List<TradeMarketDTO> markets = marketServiceClient.queryTradeMarketsByIds(tradeArea.getMarketIds());
            if (CollectionUtils.isNotEmpty(markets)) {
                try {
                    Map<String, List<TradeMarketDTO>> body = new HashMap<>();
                    body.put("markets", markets);

                    MessagePayload messagePayload = new MessagePayload();
                    messagePayload.setChannel(groupName);
                    messagePayload.setBody(objectMapper.writeValueAsString(body));
                    kafkaTemplate.send("group", new Gson().toJson(messagePayload));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        });

        RMap<Long, TradeMarketDTO> marketMap = redisson.getMap("MARKET_CACHE");
        Collection<TradeMarketDTO> markets = marketMap.values();
        if (CollectionUtils.isNotEmpty(markets)) {
            try {
                Map<String, Collection<TradeMarketDTO>> body = new HashMap<>();
                body.put("markets", markets);
                MessagePayload messagePayload = new MessagePayload();
                messagePayload.setChannel(Constant.CH_MARKETS_TICKER);
                messagePayload.setBody(objectMapper.writeValueAsString(body));
                kafkaTemplate.send("group", new Gson().toJson(messagePayload));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        marketServiceClient.tradeMarkets().forEach(t -> {
            try {
                String groupName = String.format("market.%s.detail", t.getSymbol()).toLowerCase();
                List<TradeMarketDTO> marketList = marketServiceClient.queryTradeMarketsByIds(t.getId().toString());
                Map<String, List<TradeMarketDTO>> body = new HashMap<>();
                body.put("tick", marketList);

                MessagePayload messagePayload = new MessagePayload();
                messagePayload.setChannel(groupName);
                messagePayload.setBody(objectMapper.writeValueAsString(body));
                //log.info("market.%s.detail---messagePayload------" + new Gson().toJson(messagePayload));
                kafkaTemplate.send("group", new Gson().toJson(messagePayload));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }
}