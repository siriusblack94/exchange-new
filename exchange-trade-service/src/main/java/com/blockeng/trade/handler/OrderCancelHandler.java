package com.blockeng.trade.handler;

import com.blockeng.framework.utils.GsonUtil;
import com.blockeng.trade.dto.EntrustOrderMatchDTO;
import com.blockeng.trade.service.EntrustOrderService;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;



/**
 * @Description: 撤单消息监听
 * @Author: Chen Long
 * @Date: Created in 2018/7/19 下午4:14
 * @Modified by: Chen Long
 */
@Component
@Slf4j
public class OrderCancelHandler {

    @Autowired
    private EntrustOrderService entrustOrderService;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 撤单
     *
     * @param cancelOrder 订单编号
     */
    @RabbitListener(queues = {"order.cancel"})
    public void receiveMessage(String cancelOrder) {
        EntrustOrderMatchDTO entrustOrder = GsonUtil.convertObj(cancelOrder, EntrustOrderMatchDTO.class);
        try {
            entrustOrderService.cancelEntrustOrder(entrustOrder);
        } catch (Exception e) {

//            // 撤销委托，更新MongoDB
            Query query = new Query(Criteria.where("_id").is(entrustOrder.getId()));
            mongoTemplate.remove(query, "entrust_order");
            log.error("order.cancel error" + e.getMessage());
            Document document = new Document();
            document.put("event_type", "order.cancel");
            document.put("msg", e.getMessage());
            document.put("event_data", cancelOrder);
            mongoTemplate.getCollection("events").insertOne(document);
        }
    }
}
