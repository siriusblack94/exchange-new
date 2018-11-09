package com.blockeng.task.handler;

import com.blockeng.framework.dto.CreateKLineDTO;
import com.blockeng.framework.utils.GsonUtil;
import com.lmax.disruptor.spring.boot.DisruptorTemplate;
import com.lmax.disruptor.spring.boot.event.DisruptorBindEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description: 监听消息队列生成K线
 * @Author: Chen Long
 * @Date: Created in 2018/7/1 下午2:29
 * @Modified by: Chen Long
 */

@Component
public class KLineHandler {

    @Autowired
    private DisruptorTemplate disruptorTemplate;

    /**
     * 监听消息队列生成K线
     */
    @RabbitListener(queues = {"marketdata.kline"})
    public void kline(String message) {
        CreateKLineDTO createKLineDTO = GsonUtil.convertObj(message, CreateKLineDTO.class);
        DisruptorBindEvent event = new DisruptorBindEvent(createKLineDTO, "message " + Math.random());
        event.setEvent("Event-Output");
        event.setTag("Generate-Output");
        event.setKey("id-" + Math.random());
        disruptorTemplate.publishEvent(event);
        //TradeKLineService.queue.offer(createKLineDTO);
    }
}