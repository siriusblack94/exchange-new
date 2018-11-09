package com.blockeng.task.event;

import com.alibaba.fastjson.JSONObject;
import com.blockeng.dto.TradeMarketDTO;
import com.blockeng.feign.MarketServiceClient;
import com.blockeng.framework.dto.MessagePayload;
import com.blockeng.framework.http.Response;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 推送实时成交订单数据
 *
 * @author qiang
 */
@Component
@Slf4j
public class TradeEvent implements Runnable {

    private TradeMarketDTO marketDTO;

    public TradeEvent() {
    }

    public TradeEvent(TradeMarketDTO marketDTO) {
        this.marketDTO = marketDTO;
    }

    @Autowired
    public void setMarketServiceClient(MarketServiceClient marketServiceClient) {
        TradeEvent.marketServiceClient = marketServiceClient;
    }

    @Autowired
    public void setKafkaTemplate(KafkaTemplate kafkaTemplate) {
        TradeEvent.kafkaTemplate = kafkaTemplate;
    }

    @Autowired
    private static MarketServiceClient marketServiceClient;

    @Autowired
    private static KafkaTemplate kafkaTemplate;

    @Override
    public void run() {
        String groupName = String.format("market.%s.trade.detail", marketDTO.getSymbol()).toLowerCase();
        Response response = marketServiceClient.trades(marketDTO.getSymbol());
        if (Optional.ofNullable(response.getData()).isPresent()) {
            JSONObject orders = new JSONObject();
            orders.put("data", response.getData());
            MessagePayload messagePayload = new MessagePayload();
            messagePayload.setChannel(groupName);
            messagePayload.setBody(orders.toJSONString());
//            log.info("推送实时成交订单数据Channel="+messagePayload.getChannel()+"data");
            kafkaTemplate.send("group", new Gson().toJson(messagePayload));
        }
    }
}
