package com.blockeng.core.handlers;

import com.blockeng.boot.LoadOrderDataRunner;
import com.blockeng.data.MatchData;
import com.blockeng.model.Order;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author qiang
 */
@Component
@Slf4j
@org.springframework.core.annotation.Order(-1)
public class OrderInHandler {

    /**
     * 委托订单入队列
     *
     * @param payload
     */
    public static AtomicInteger size = new AtomicInteger(0);


    @RabbitListener(queues = "order.in", concurrency = "24")
    public void in(String payload) {

        size.getAndIncrement();
        Order orderVo = new Gson().fromJson(payload, Order.class);

        //防止重启后，数据库与mq中数据重复
        if (LoadOrderDataRunner.mysqlLoadData.containsKey(orderVo.getId()) && orderVo.getStatus().intValue() != 2) {
            log.debug("Rabbit listener receive and refused:{}", orderVo);
            return;
        }

        log.debug("Rabbit listener receive:{}", orderVo);
        MatchData.queue.offer(orderVo);
    }
}
