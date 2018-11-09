package com.blockeng.task.event;

import com.alibaba.fastjson.JSONObject;
import com.blockeng.dto.MarketDTO;
import com.blockeng.dto.TradeMarketDTO;
import com.blockeng.feign.MarketServiceClient;
import com.blockeng.framework.dto.MessagePayload;
import com.blockeng.framework.enums.DepthMergeType;
import com.blockeng.framework.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 推送市场合并深度
 *
 * @author qiang
 */
@Component
@Slf4j
public class DepthEvent implements Runnable {

    private TradeMarketDTO marketDTO;

    private DepthMergeType depthMergeType;

    @Autowired
    public void setMarketServiceClient(MarketServiceClient marketServiceClient) {
        DepthEvent.marketServiceClient = marketServiceClient;
    }

    @Autowired
    public void setKafkaTemplate(KafkaTemplate kafkaTemplate) {
        DepthEvent.kafkaTemplate = kafkaTemplate;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        DepthEvent.objectMapper = objectMapper;
    }

    private static MarketServiceClient marketServiceClient;

    private static KafkaTemplate kafkaTemplate;

    private static ObjectMapper objectMapper;

    public DepthEvent() {
    }

    public DepthEvent(TradeMarketDTO marketDTO, DepthMergeType depthMergeType) {
        this.marketDTO = marketDTO;
        this.depthMergeType = depthMergeType;
    }

    @Autowired
    private RedissonClient redisson;

    /**
     * 推送市场合并深度
     */
    @Override
    public void run() {
        try {
            String groupName = String.format("market.%s.depth.step%s", marketDTO.getSymbol(), depthMergeType.getValue()).toLowerCase();
            JSONObject body = new JSONObject();
            Response response = marketServiceClient.depth(marketDTO.getSymbol(), depthMergeType.getCode());
            if (Optional.ofNullable(response.getData()).isPresent()) {
                body.put("tick", response.getData());
                MessagePayload messagePayload = new MessagePayload();
                messagePayload.setChannel(groupName);
                messagePayload.setBody(objectMapper.writeValueAsString(body));
//                log.info("推送市场合并深度Channel="+messagePayload.getChannel()+"tick");
                kafkaTemplate.send("group", new Gson().toJson(messagePayload));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}