package com.blockeng.handle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.blockeng.websocket.ChannelWebsocketStarter;
import com.blockeng.websocket.vo.ResponseEntity;
import com.blockeng.framework.constants.Constant;
import com.blockeng.framework.dto.MessagePayload;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.tio.core.Tio;
import org.tio.server.ServerGroupContext;

import java.util.Map;
import java.util.Optional;

/**
 * 从Channel接收并处理消息
 *
 * @author qiang
 */
@Component
@Slf4j
public class MessageReceiver {

    @KafkaListener(topics = {Constant.CHANNEL_SENDTO_USER})
    public void convertAndSendToUser(String payload) {
        MessagePayload messagePayload = new Gson().fromJson(payload, MessagePayload.class);
        ServerGroupContext serverGroupContext = ChannelWebsocketStarter.getServerGroupContext();
        if (Optional.ofNullable(serverGroupContext).isPresent()) {
            ResponseEntity responseEntity = new ResponseEntity()
                    .setId(serverGroupContext.getId())
                    .setEvent(messagePayload.getChannel());
            try {
                responseEntity.putAll(JSON.parseObject(messagePayload.getBody(), Map.class));
            } catch (JSONException e) {
                responseEntity.put("result", messagePayload.getBody());
            }
            Tio.sendToUser(serverGroupContext, messagePayload.getUserId(), responseEntity.build());
        }
    }

    @KafkaListener(topics = {Constant.CHANNEL_SENDTO_GROUP})
    public void convertAndSendToGroup(String payload) {
//        log.info("group---"+payload);
        MessagePayload messagePayload = new Gson().fromJson(payload, MessagePayload.class);
        ServerGroupContext serverGroupContext = ChannelWebsocketStarter.getServerGroupContext();
        if (Optional.ofNullable(serverGroupContext).isPresent()) {
            ResponseEntity responseEntity = new ResponseEntity()
                    .setId(serverGroupContext.getId())
                    .setCh(messagePayload.getChannel());
            try {
                responseEntity.putAll(JSON.parseObject(messagePayload.getBody(), Map.class));
            } catch (JSONException e) {
                responseEntity.put("result", messagePayload.getBody());
            }
            Tio.sendToGroup(serverGroupContext, messagePayload.getChannel(), responseEntity.build());
        }
    }
}