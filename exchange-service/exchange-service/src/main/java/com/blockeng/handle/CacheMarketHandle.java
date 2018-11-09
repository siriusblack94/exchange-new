package com.blockeng.handle;

import com.alibaba.fastjson.JSONObject;
import com.blockeng.dto.MatchDTO;
import com.blockeng.entity.Market;
import com.blockeng.framework.constants.Constant;
import com.blockeng.framework.dto.MessagePayload;
import com.blockeng.service.MarketService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @Description: 撮合成功后更新缓存数据
 * @Author: Chen Long
 * @Date: Created in 2018/5/20 下午4:55
 * @Modified by: Chen Long
 */
@Component
@Slf4j
public class CacheMarketHandle {

    @Autowired
    private MarketService marketService;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    /**
     * 币币交易撮合成功后
     *
     * @return
     */
    public void refreshMarket(MatchDTO matchDTO) {
        Market market = marketService.queryBySymbol(matchDTO.getSymbol());
        // 更新交易对行情
        marketService.refreshLatestPrice(market, matchDTO.getPrice());

        // 通知用户拉取最新的委托单和成交单
        this.pushUserTradeEntrustOrder(matchDTO.getBuyUserId(), matchDTO.getSymbol());
        if (!matchDTO.getBuyUserId().equals(matchDTO.getSellUserId())) {
            this.pushUserTradeEntrustOrder(matchDTO.getSellUserId(), matchDTO.getSymbol());
        }
    }

    /**
     * 推送用户币币交易委托订单数据
     *
     * @param userId 用户ID
     */
    private void pushUserTradeEntrustOrder(Long userId, String symbol) {
        // 推送用户未成交委托
        JSONObject body = new JSONObject();
        body.put("symbol", symbol);
        // 通知用户拉取未成交委托
        MessagePayload messagePayload = new MessagePayload();
        messagePayload.setUserId(String.valueOf(userId));
        messagePayload.setChannel(Constant.CH_ORDER_PENDING);
        messagePayload.setBody(body.toJSONString());
        kafkaTemplate.send(Constant.CHANNEL_SENDTO_USER, new Gson().toJson(messagePayload));

        // 通知用户拉取历史委托
        messagePayload = new MessagePayload();
        messagePayload.setUserId(String.valueOf(userId));
        messagePayload.setChannel(Constant.CH_ORDER_FINISHED);
        messagePayload.setBody(body.toJSONString());
        kafkaTemplate.send(Constant.CHANNEL_SENDTO_USER, new Gson().toJson(messagePayload));
    }
}
