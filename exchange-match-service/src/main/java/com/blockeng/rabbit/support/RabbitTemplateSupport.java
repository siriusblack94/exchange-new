package com.blockeng.rabbit.support;

import lombok.Data;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author maple
 * @date 2018/10/23 11:32
 **/
@Data
public class RabbitTemplateSupport {

    /**
     * 消息缓存
     */
    final private Map<String, MessageAndTimes> messageMap = new ConcurrentHashMap<>();
    /**
     * 不解释
     */
    private RabbitTemplate template;

    /**
     * 包装template模板的发送方法
     *
     * @param routKey 路由
     * @param message 消息
     */
    public void convertAndSend(String routKey, Object message) {
        CorrelationData correlationData = new CorrelationData();//confirm功能中回调可返回的重要对象
        correlationData.setId(UUID.randomUUID().toString()); //动态uuid
        messageMap.put(correlationData.getId(), new MessageAndTimes(0, message, "", routKey));//缓存消息
        template.convertAndSend("", routKey, message, correlationData);//发送
    }

}
