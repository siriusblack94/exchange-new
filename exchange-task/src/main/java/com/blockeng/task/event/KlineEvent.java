package com.blockeng.task.event;

import com.alibaba.fastjson.JSONObject;
import com.blockeng.framework.dto.MessagePayload;
import com.blockeng.framework.enums.KlineType;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * 每5秒推送K线数据
 *
 * @author qiang
 */
@Component
@Slf4j
public class KlineEvent implements Runnable {

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        KlineEvent.redisTemplate = redisTemplate;
    }

    private static RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public void setKafkaTemplate(KafkaTemplate kafkaTemplate) {
        KlineEvent.kafkaTemplate = kafkaTemplate;
    }

    private static KafkaTemplate kafkaTemplate;

    /**
     * 交易对标识符
     */
    private String symbol;

    /**
     * 通道
     */
    private String channel;

    /**
     * redis key 前缀
     */
    private String keyPrefix;

    public KlineEvent() {
    }

    public KlineEvent(String symbol, String channel, String keyPrefix) {
        this.symbol = symbol;
        this.channel = channel;
        this.keyPrefix = keyPrefix;
    }

    @Override
    public void run() {
        for (KlineType klineType : KlineType.values()) {
           // log.info("3秒一送执行了么？--klineType--"+klineType);
            String groupName = String.format(channel, symbol.toLowerCase(), klineType.getValue()).toLowerCase();

            String key = new StringBuffer(keyPrefix).append(symbol).append(":").append(klineType.getValue()).toString();

            List<Object> klines = redisTemplate.opsForList().range(key, 0, 999);

            if (!CollectionUtils.isEmpty(klines)) {
                Collections.reverse(klines);
                MessagePayload messagePayload = new MessagePayload();
                messagePayload.setChannel(groupName);
                JSONObject body = new JSONObject();
                body.put("tick", klines);
                messagePayload.setBody(body.toJSONString());
               // log.info("3秒一送执行了么？--Channel--"+messagePayload.getChannel()+"--tick");
               // log.info("3秒一送执行了么？--conent--"+new Gson().toJson(messagePayload));
                kafkaTemplate.send("group", new Gson().toJson(messagePayload));
            }
        }
    }
}