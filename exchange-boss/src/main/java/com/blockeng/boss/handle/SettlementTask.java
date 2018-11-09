package com.blockeng.boss.handle;

import com.alibaba.fastjson.JSONObject;
import com.blockeng.boss.dto.DealOrder;
import com.blockeng.boss.dto.EntrustOrderMatchDTO;
import com.blockeng.boss.dto.EntrustOrderMatchDTOMapper;
import com.blockeng.boss.mapper.EntrustOrderMapper;
import com.blockeng.boss.repository.DealOrderRepository;
import com.blockeng.boss.service.BossService;
import com.blockeng.framework.constants.Constant;
import com.blockeng.framework.dto.MatchDTO;
import com.blockeng.framework.dto.MessagePayload;
import com.blockeng.framework.exception.ExchangeException;
import com.google.gson.Gson;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/7/11 上午4:43
 * @Modified by: Chen Long
 */
@Component
public class SettlementTask implements Constant {

    @Autowired
    private BossService bossService;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private DealOrderRepository dealOrderRepository;

    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * 资金清算
     *
     * @param dealOrder
     */
    @Async
    @Retryable(value = ExchangeException.class, maxAttempts = 5, backoff = @Backoff(delay = 1000, multiplier = 1.5))
    public void settlement(DealOrder dealOrder) {
        // 资金清算
        boolean result = bossService.settlement(dealOrder);
        if (result) {
            // 保存成交记录到MongoDB
            dealOrderRepository.save(dealOrder);
            BigDecimal buyVolume = dealOrder.getBuyVolume();
            BigDecimal sellVolume = dealOrder.getSellVolume();
            if (buyVolume.compareTo(sellVolume) == 1) {
                // 买单量 > 卖单量
                // 修改买单委托量
                Query query = new Query(Criteria.where("_id").is(dealOrder.getBuyOrderId()));
                Update update = new Update().set("volume", buyVolume.subtract(dealOrder.getVol()));
                mongoTemplate.updateFirst(query, update, "entrust_order");
                // 已成交,移除卖单
                mongoTemplate.remove(new Query(Criteria.where("_id").is(dealOrder.getSellOrderId())), "entrust_order");
            } else if (buyVolume.compareTo(sellVolume) == -1) {
                // 卖单量 > 买单量
                Query query = new Query(Criteria.where("_id").is(dealOrder.getSellOrderId()));
                Update update = new Update().set("volume", sellVolume.subtract(dealOrder.getVol()));
                mongoTemplate.updateFirst(query, update, "entrust_order");
                // 更新买单为已成交
                query = new Query(Criteria.where("_id").is(dealOrder.getBuyOrderId()));
                // 更新卖单为已成交
                update = new Update().set("status", 1);
                mongoTemplate.updateFirst(query, update, "entrust_order");
            } else {
                // 卖单量 == 买单量
                Query query = new Query(Criteria.where("_id").in(dealOrder.getBuyOrderId(), dealOrder.getSellOrderId()));
                mongoTemplate.remove(query, "entrust_order");
            }
            // 更新交易对行情
            MatchDTO matchDTO = new MatchDTO()
                    .setSymbol(dealOrder.getSymbol())
                    .setBuyUserId(dealOrder.getBuyUserId())
                    .setSellUserId(dealOrder.getSellUserId())
                    .setPrice(dealOrder.getPrice());
            kafkaTemplate.send("ticker", new Gson().toJson(matchDTO));
        }
    }

    public void cancel(EntrustOrderMatchDTO cancelOrder) {
        if (bossService.cancel(cancelOrder)) {

            Query query = new Query(Criteria.where("_id").is(cancelOrder.getId()));
            mongoTemplate.remove(query, "entrust_order");
            // 推送用户未成交委托
            JSONObject body = new JSONObject();
            body.put("symbol", cancelOrder.getSymbol());
            MessagePayload messagePayload = new MessagePayload();
            messagePayload.setUserId(String.valueOf(cancelOrder.getUserId()));
            messagePayload.setChannel(CH_ORDER_PENDING);
            messagePayload.setBody(body.toJSONString());
            kafkaTemplate.send(CHANNEL_SENDTO_USER, new Gson().toJson(messagePayload));
            messagePayload = new MessagePayload();
            messagePayload.setUserId(String.valueOf(cancelOrder.getUserId()));
            messagePayload.setChannel(CH_ORDER_FINISHED);
            messagePayload.setBody(body.toJSONString());
            kafkaTemplate.send(CHANNEL_SENDTO_USER, new Gson().toJson(messagePayload));
        }
    }
}
