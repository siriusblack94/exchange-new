package com.blockeng.boss.handle;

import com.blockeng.boss.dto.DealOrder;
import com.blockeng.boss.dto.EntrustOrderMatchDTO;
import com.blockeng.boss.dto.EntrustOrderMatchDTOMapper;
import com.blockeng.framework.exception.ExchangeException;
import com.blockeng.framework.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/6/17 下午4:51
 * @Modified by: Chen Long
 */
@Component
@Slf4j
public class MatchHandle {

    @Autowired
    private SettlementTask settlementTask;

    @Autowired
    private MongoTemplate mongoTemplate;

    private ExecutorService executor = new ThreadPoolExecutor(
            500,
            Integer.MAX_VALUE,
            60, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1024),
            new ThreadPoolExecutor.CallerRunsPolicy());

    @Bean("orderTxTaskContainerFactory")
    public SimpleRabbitListenerContainerFactory pointTaskContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setPrefetchCount(64);
        factory.setConcurrentConsumers(64);
        factory.setMaxConcurrentConsumers(64);
        factory.setTaskExecutor(executor);
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    /**
     * 币币交易撮合成交后，资金清算
     *
     * @param tx
     */
    @RabbitListener(queues = {"order.tx"}, containerFactory = "orderTxTaskContainerFactory")
    public void receiveMessage(String tx) {

        DealOrder dealOrder = GsonUtil.convertObj(tx, DealOrder.class);

        if (dealOrder.isCancel()) {
            EntrustOrderMatchDTO entrustOrder = EntrustOrderMatchDTOMapper.INSTANCE.fromBuyOrder(dealOrder);
            try {
                  settlementTask.cancel(entrustOrder);
            } catch (Exception e) {
                // 撤销委托，更新MongoDB
                Query query = new Query(Criteria.where("_id").is(entrustOrder.getId()));
                mongoTemplate.remove(query, "entrust_order");
                log.error("order.cancel error" + e.getMessage());
                Document document = new Document();
                document.put("event_type", "order.cancel");
                document.put("msg", e.getMessage());
                document.put("event_data", GsonUtil.toJson(entrustOrder));
                mongoTemplate.getCollection("events").insertOne(document);
            }
            return;
        }

        try {
            // 撮合委托订单
            settlementTask.settlement(dealOrder);
        } catch (Exception e) {
            log.error("资金清算异常" + e.getMessage());
            Document document = new Document();
            document.put("event_type", "order.tx");
            document.put("msg", e.getMessage());
            document.put("event_data", GsonUtil.toJson(dealOrder));
            mongoTemplate.getCollection("events").insertOne(document);
        }
    }
}
