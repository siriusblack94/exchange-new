package com.blockeng.service.impl;

import com.blockeng.entity.Config;
import com.blockeng.framework.enums.BusinessType;
import com.blockeng.framework.enums.MiningConfig;
import com.blockeng.service.AccountService;
import com.blockeng.service.BonusService;
import com.blockeng.service.ConfigService;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.Decimal128;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author qiang
 */
@Service
@Slf4j
public class BonusServiceImpl implements BonusService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ConfigService configService;

    @Autowired
    private AccountService accountService;

    /**
     * 交易挖矿返还
     *
     * @param date
     */
    @Override
    public void tradingDigSet(String date) {
        Config config = configService.queryByTypeAndCode(MiningConfig.TYPE.getValue(), MiningConfig.CODE_COIN_ID.getValue());
        Long coinId = Long.parseLong(config.getValue());
        Document query = new Document();
        query.put("date", date);
        query.put("rt", 0);
        MongoCollection tradingRefundTotalCollection = mongoTemplate.getCollection("trading_refund_total");
        tradingRefundTotalCollection.find(query).forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                long userId = document.getLong("user_id");

                Document q = new Document();
                query.put("user_id", userId);
                query.put("rt", 0);
                document.put("rt", 1);
                tradingRefundTotalCollection.updateOne(q, document);

                BigDecimal act = document.get("act", Decimal128.class).bigDecimalValue().setScale(8, BigDecimal.ROUND_HALF_UP);
                accountService.addAmount(userId, coinId, act, BusinessType.TRADING_DIG, "交易挖矿(" + date + ")", 0L);
            }
        });
    }

    /**
     * 分红返还
     *
     * @param date
     */
    @Override
    public void bonusSet(String date) {
        Config config = configService.queryByTypeAndCode(MiningConfig.TYPE.getValue(), MiningConfig.CODE_COIN_ID.getValue());
        Long coinId = Long.parseLong(config.getValue());
        MongoCollection todayDividendCollection = mongoTemplate.getCollection("today_dividend");
        Document query = new Document();
        query.put("date", date);
        query.put("rt", 0);
        todayDividendCollection.find(query).forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                long userId = document.getLong("user_id");
                Document q = new Document();
                query.put("user_id", userId);
                query.put("rt", 0);
                query.put("date", date);
                document.put("rt", 1);

                Document u = new Document();
                u.put("$set", new Document("rt", 1));
                todayDividendCollection.updateOne(q, u);
                BigDecimal act = document.get("amount", Decimal128.class).bigDecimalValue().setScale(8, BigDecimal.ROUND_HALF_UP);
                accountService.addAmount(userId, coinId, act, BusinessType.BONUS, "分红(" + date + ")", 0L);
            }
        });
    }

    @Override
    public void inviteRewardsSet(String date) {

    }
}
