package com.blockeng.task.task;

import com.blockeng.framework.constants.Constant;
import com.blockeng.framework.enums.MiningConfig;
import com.blockeng.task.entity.Config;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.bson.BsonNull;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Decimal128;
import org.joda.time.DateTime;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @Description: 挖矿
 * @Author: Chen Long
 * @Date: Created in 2018/6/26 下午7:32
 * @Modified by: Chen Long
 */
@Component
@Slf4j
public class MiningTask {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 1、交易挖矿
     * 用户交易手续费返还
     * 用户当日交易手续费，折算成平台币的价值，100%返回成平台币（手续费*成交均价）；
     * 返还的价值不超过前日资产的20%；
     * <p>
     * 2、用户分红(所有用户)
     * 根据用户当前持币量来进行分红
     * 平台币总流通量：系统上线以来挖矿总和（返还的平台币总和） * 2
     * 每日分红 = (平台前一日挖矿总和 * 80%) * (个人持有平台币数量 / 平台币总流通量)
     * <p>
     * 3、邀请奖励
     * 多级账户邀请体系，例如：A邀请B，B邀请C；C邀请D，B邀请E；
     * 当前为A用户：
     * 如果A邀请的用户数 > 20， 则A用户的邀请奖励为：A的直接下级每日挖矿奖励总和（用户挖矿实际得到） * 20%；
     * 如果A邀请的用户数 <= 20，则A用户的邀请奖励为：A的直接下级每日挖矿奖励总和（用户挖矿实际得到） * 10%；
     * 邀请奖励冻结1周，下周一可以解冻，解冻资金上线不能超过用户上周挖矿总量
     */
    //@Scheduled(cron = "0 0 0 * * ?")
    public void mining() {
        // 判断是否开启交易挖矿
        Criteria criteria = Criteria.where("type").is(MiningConfig.TYPE.getValue())
                .and("code").is(MiningConfig.CODE_SWITCH.getValue());
        Query query = new Query(criteria);
        Config config = mongoTemplate.findOne(query, Config.class);
        if (config == null || StringUtils.isEmpty(config.getValue()) || !MiningConfig.OPEN.getValue().equals(config.getValue())) {
            return;
        }
        String date = new DateTime().minusDays(1).toString("yyyyMMdd");
        // 快照用户资产account=>account_(date)
        log.info("快照用户资产");
        snapshoot(date);

        // 定时任务3：统计当天交易的用户的手续费account_detail=>datacube_trading_fee
        log.info("开始统计当天交易的用户的手续费");
        calcTradingFee(date);

        // 平台币的CoinId
        criteria = Criteria.where("type").is(MiningConfig.TYPE.getValue())
                .and("code").is(MiningConfig.CODE_COIN_ID.getValue());
        query = new Query(criteria);
        config = mongoTemplate.findOne(query, Config.class);
        long coinId = Long.parseLong(config.getValue());

        // 定时任务2：交易对日成交均价
        log.info("开始统计交易对日成交均价");
        avgPrice(coinId, date);
        // 定时任务5：计算手续费挖矿
        //交易挖矿
        dig(coinId, date);
        // 定时任务4：计算分红
        //计算平台币总流通量
        bonus(date);
        // 定时任务6：计算邀请奖励
        inviteRewards(date);

        log.info("通知清算系统");
        //rabbitTemplate.convertAndSend("bonus", date);
        log.info("Succeeded!");
    }

    /**
     * 交易挖矿
     */
    public void dig(long coinId, String date) {
        //计算用户总资产（换算成平台币）
        log.info("开始计算用户总资产（换算成平台币）");
        calculateAssets(coinId, date);
        //计算返还的价值（换算成平台币）
        log.info("开始计算返还的价值（换算成平台币）");
        calculateRefund(coinId, date);
        log.info("开始计算所有币种返还的价值（换算成平台币）");
        calculateTotalRefund(date);
    }

    /**
     * 分红
     */
    public void bonus(String date) {
        log.info("开始计算平台币总流通量");
        calculateMiningSum(date);
        calculateMiningTotal();

        //计算每日分红
        log.info("开始计算每日分红");
        calculateTodayDividend(date);
    }

    /**
     * 邀请奖励
     */
    public void inviteRewards(String date) {
        //统计邀请人数
        log.info("开始统计邀请人数");
        countInvitation();
        //计算邀请奖励
        log.info("开始计算邀请奖励");
        calculateInviteRewards(date);
        //更新邀请奖励用户
        countInviteRewardsAccount();
    }

    private void countInviteRewardsAccount() {
        MongoCollection<Document> inviteRewardsCollection = mongoTemplate.getCollection("invite_rewards");

        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$project", new Document()
                                .append("id", 1.0)
                                .append("amount", 1)
                                .append("user_id", 1)
                        ),
                new Document()
                        .append("$match", new Document()
                                .append("user_id", 1013084774948749314L)
                        ),
                new Document()
                        .append("$group", new Document()
                                .append("_id", "$user_id")
                                .append("amount", new Document()
                                        .append("$sum", "$amount")
                                )
                        ),
                new Document()
                        .append("$project", new Document()
                                .append("user_id", "$_id")
                                .append("amount", "$amount")
                        )
        );

        inviteRewardsCollection.aggregate(pipeline)
                .allowDiskUse(false)
                .forEach(new Block<Document>() {
                    @Override
                    public void apply(final Document document) {
                        Long userId = document.getLong("user_id");
                        BigDecimal amount = document.get("amount", Decimal128.class).bigDecimalValue();
                        Query query = new Query(Criteria.where("user_id").is(userId));

                        Document inviteRewardsAccount = mongoTemplate.getCollection("invite_rewards_account").find(new Document("user_id", userId)).first();
                        if (Optional.ofNullable(inviteRewardsAccount).isPresent()) {
                            Document update = new Document();
                            update.put("$inc", new Document("amount", amount));

                            //本周奖励
                            BigDecimal weekRewarded = countWeekRewarded(userId);
                            BigDecimal rewardsAmount = inviteRewardsAccount.get("amount", Decimal128.class).bigDecimalValue();
                            BigDecimal thawed = inviteRewardsAccount.get("thawed", Decimal128.class).bigDecimalValue();
                            BigDecimal canDefrost = rewardsAmount.subtract(thawed).subtract(weekRewarded);
                            //上周挖矿量
                            BigDecimal lastWeekMining = countLastWeekMining(userId);
                            if (canDefrost.compareTo(lastWeekMining) > 0) {
                                canDefrost = lastWeekMining;
                            }

                            update.put("can_defrost", canDefrost);//可解冻奖励
                            mongoTemplate.updateFirst(query, Update.fromDocument(update), "invite_rewards_account");
                        } else {
                            Document ira = new Document();
                            ira.put("user_id", userId);
                            ira.put("amount", amount);//总冻结
                            ira.put("freeze", BigDecimal.ZERO);//冻结量
                            ira.put("thawed", BigDecimal.ZERO);//已经解冻量
                            ira.put("can_defrost", BigDecimal.ZERO);//可解冻奖励
                            mongoTemplate.save(ira, "invite_rewards_account");
                        }
                    }
                });
    }

    /**
     * 统计上周挖矿量
     *
     * @param userId
     * @return
     */
    private BigDecimal countLastWeekMining(Long userId) {
        List<String> dates = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            String date = new DateTime().minusWeeks(1).withDayOfWeek(1).toString("yyyyMMdd");
            dates.add(date);
        }

        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$project", new Document()
                                .append("user_id", 1.0)
                                .append("act", 1.0)
                                .append("date", 1.0)
                        ),
                new Document()
                        .append("$match", new Document()
                                .append("user_id", userId)
                                .append("date", new Document()
                                        .append("$in", dates
                                        )
                                )
                        ),
                new Document()
                        .append("$group", new Document()
                                .append("_id", "$user_id")
                                .append("amount", new Document()
                                        .append("$sum", "$act")
                                )
                        ),
                new Document()
                        .append("$project", new Document()
                                .append("user_id", "$_id")
                                .append("amount", "$amount")
                        )
        );

        Document document = mongoTemplate.getCollection("trading_refund_total").aggregate(pipeline)
                .allowDiskUse(false).first();
        BigDecimal amount = BigDecimal.ZERO;
        if (Optional.ofNullable(document).isPresent()) {
            amount = document.get("amount", Decimal128.class).bigDecimalValue();
        }
        return amount;
    }

    /**
     * 本周奖励
     *
     * @param userId
     * @return
     */
    private BigDecimal countWeekRewarded(Long userId) {
        List<String> dates = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            String date = new DateTime().withDayOfWeek(1).toString("yyyyMMdd");
            dates.add(date);
        }

        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$project", new Document()
                                .append("id", 1.0)
                                .append("amount", 1.0)
                                .append("freeze", 1.0)
                                .append("user_id", 1.0)
                                .append("date", 1.0)
                        ),
                new Document()
                        .append("$match", new Document()
                                .append("user_id", userId)
                                .append("date", new Document()
                                        .append("$in", dates
                                        )
                                )
                        ),
                new Document()
                        .append("$group", new Document()
                                .append("_id", "$user_id")
                                .append("amount", new Document()
                                        .append("$sum", "$amount")
                                )
                        ),
                new Document()
                        .append("$project", new Document()
                                .append("user_id", "$_id")
                                .append("amount", "$amount")
                        )
        );

        Document document = mongoTemplate.getCollection("invite_rewards").aggregate(pipeline)
                .allowDiskUse(false).first();
        BigDecimal amount = BigDecimal.ZERO;
        if (Optional.ofNullable(document).isPresent()) {
            amount = document.get("amount", Decimal128.class).bigDecimalValue();
        }
        return amount;
    }

    //计算每日分红(每日分红 = (平台前一日挖矿总和 * 80%) * (个人持有平台币数量 / 平台币总流通量))
    public void calculateTodayDividend(String date) {
        Document msDoc = mongoTemplate.getCollection("mining_sum").find(new Document("date", date)).first();
        Document mstDoc = mongoTemplate.getCollection("mining_sum_total").find().first();

        if (!Optional.ofNullable(msDoc).isPresent()) {
            log.info("前一日挖矿总和未统计（为空）");
            return;
        }
        //平台前一日挖矿总和
        BigDecimal amount = msDoc.get("amount", Decimal128.class).bigDecimalValue().setScale(8, BigDecimal.ROUND_HALF_UP);
        //平台币总流通量
        BigDecimal total = mstDoc.get("total", Decimal128.class).bigDecimalValue().setScale(8, BigDecimal.ROUND_HALF_UP);

        mongoTemplate.getCollection("datacube_dig_account").find(new Document("date", date)).forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                long userId = document.getLong("user_id");
                BigDecimal personalAssets = document.get("amount", Decimal128.class).bigDecimalValue().setScale(8, BigDecimal.ROUND_HALF_UP);
                BigDecimal todayDividend = amount.multiply(new BigDecimal(0.8).multiply(personalAssets.divide(total, 8, BigDecimal.ROUND_HALF_UP))).setScale(8, BigDecimal.ROUND_HALF_UP);

                Document todayDividendDoc = new Document();
                todayDividendDoc.put("user_id", userId);
                todayDividendDoc.put("amount", todayDividend);
                todayDividendDoc.put("rt", 0);
                todayDividendDoc.put("date", date);
                mongoTemplate.insert(todayDividendDoc, "today_dividend");
            }
        });


/*        mongoTemplate.getCollection("trading_refund_total").find().forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {

            }
        });*/
    }

    /**
     * 资产快照
     */
    public void snapshoot(String date) {
        MongoCollection<Document> collection = mongoTemplate.getCollection("account");

        Block<Document> processBlock = new Block<Document>() {
            @Override
            public void apply(final Document document) {

            }
        };

        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$project", new Document()
                                .append("id", 1)
                                .append("status", 1)
                                .append("balance_amount", 1)
                                .append("freeze_amount", 1)
                                .append("recharge_amount", 1)
                                .append("withdrawals_amount", 1)
                                .append("net_value", 1)
                                .append("lock_margin", 1)
                                .append("float_profit", 1)
                                .append("total_profit", 1)
                                .append("rec_addr", 1)
                                .append("version", 1)
                                .append("created", 1)
                                .append("coin_id", 1)
                                .append("user_id", 1)
                                .append("amount", new Document()
                                        .append("$add", Arrays.asList(
                                                "$balance_amount",
                                                "$freeze_amount"
                                                )
                                        )
                                )
                                .append("last_update_time", 1)
                        ),
                new Document()
                        .append("$out", "account." + date)
        );

        collection.aggregate(pipeline)
                .allowDiskUse(false)
                .forEach(processBlock);
    }

    /**
     * 成交均价
     */
    public void avgPrice(Long coinId, String date) {
        MongoCollection turnoverOrderCollection = mongoTemplate.getCollection("turnover_order");
        mongoTemplate.remove(new Query(Criteria.where("date").is(date)), "datacube_deal_prices");

        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$project", new Document()
                                .append("symbol", 1)
                                .append("amount", 1)
                                .append("volume", 1)
                                .append("created", 1)
                                .append("sell_coin_id", 1)
                                .append("buy_coin_id", 1)
                        ),
                new Document()
                        .append("$match", new Document()
                                .append("sell_coin_id", coinId)
                                .append("created", new Document()
                                        .append("$gte", new DateTime().minusDays(10).toDate())
                                        .append("$lte", new DateTime().toDate())
                                )
                        ),
                new Document()
                        .append("$group", new Document()
                                .append("_id", new Document()
                                        .append("buy_coin_id", "$buy_coin_id")
                                        .append("sell_coin_id", "$sell_coin_id")
                                        .append("symbol", "$symbol")
                                )
                                .append("amount", new Document()
                                        .append("$sum", "$amount")
                                )
                                .append("vol", new Document()
                                        .append("$sum", "$volume")
                                )
                        ),
                new Document()
                        .append("$project", new Document()
                                .append("symbol", "$_id.symbol")
                                .append("amount", "$amount")
                                .append("vol", "$vol")
                                .append("avg_price", new Document()
                                        .append("$divide", Arrays.asList(
                                                "$amount",
                                                "$vol"
                                                )
                                        )
                                )
                                .append("sell_coin_id", "$_id.sell_coin_id")
                                .append("buy_coin_id", "$_id.buy_coin_id")
                        )
        );

        turnoverOrderCollection.aggregate(pipeline)
                .allowDiskUse(false)
                .forEach(new Block<Document>() {
                    @Override
                    public void apply(final Document document) {
                        document.remove("_id");
                        document.put("date", date);
                        mongoTemplate.insert(document, "datacube_deal_prices");
                    }
                });
    }


    public MongoCollection getAccountSnap(String date) {
        return mongoTemplate.getCollection("account." + date);
    }

    /**
     * 统计当前参与交易的用户
     */
    public void calcTradingFee(String date) {
        MongoCollection accountCollection = mongoTemplate.getCollection("account_detail");
        mongoTemplate.remove(new Query(Criteria.where("date").is(date)), "datacube_trading_fee");

        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$project", new Document()
                                .append("user_id", 1.0)
                                .append("coin_id", 1.0)
                                .append("created", 1.0)
                                .append("fee", 1.0)
                                .append("last_update_time", 1)
                        ),
                new Document()
                        .append("$match", new Document()
                                .append("created", new Document()
                                        .append("$gte", new DateTime().minusDays(10).toDate())
                                        .append("$lte", new DateTime().toDate())
                                )
                        ),
                new Document()
                        .append("$group", new Document()
                                .append("_id", new Document()
                                        .append("user_id", "$user_id")
                                        .append("coin_id", "$coin_id")
                                )
                                .append("fee", new Document()
                                        .append("$sum", "$fee")
                                )
                        ),
                new Document()
                        .append("$project", new Document()
                                .append("user_id", "$_id.user_id")
                                .append("coin_id", "$_id.coin_id")
                                .append("fee", "$fee")
                                .append("date", date)
                        )
        );

        accountCollection.aggregate(pipeline)
                .allowDiskUse(false)
                .forEach(new Block<Document>() {
                    @Override
                    public void apply(final Document document) {
                        document.remove("_id");
                        mongoTemplate.insert(document, "datacube_trading_fee");
                    }
                });
    }

    /**
     * 计算用户总资产，折算成平台币的价值
     *
     * @param coinId
     */
    public void calculateAssets(Long coinId, String date) {
        Config config = mongoTemplate.findOne(new Query(Criteria.where("type").is(Constant.CONFIG_TYPE_SYSTEM)
                .and("code").is(Constant.C2C_ADMIN_USER_ID)), Config.class);
        Long c2cUserId = Long.valueOf(config.getValue());

        MongoCollection<Document> accountCollection = mongoTemplate.getCollection("account." + date);

        mongoTemplate.remove(new Query(Criteria.where("ref_date").is(date)), "dig_account");
        Document query = new Document();
        query.append("sell_coin_id", coinId);
        mongoTemplate.getCollection("datacube_deal_prices").find(query).forEach(new Block<Document>() {

            @Override
            public void apply(Document document) {
                BigDecimal avgPrice = document.get("avg_price", Decimal128.class).bigDecimalValue();
                long sellCoinId = document.getLong("sell_coin_id");

                List<? extends Bson> pipeline = Arrays.asList(
                        new Document()
                                .append("$project", new Document()
                                        .append("coin_id", 1)
                                        .append("user_id", 1)
                                        .append("amount", 1)
                                ),
                        new Document()
                                .append("$match", new Document()
                                        .append("user_id", new Document()
                                                .append("$nin", Arrays.asList(
                                                        c2cUserId
                                                        )
                                                )
                                        )
                                        .append("coin_id", sellCoinId)
                                ),
                        new Document()
                                .append("$group", new Document()
                                        .append("_id", new Document()
                                                .append("user_id", "$user_id")
                                                .append("coin_id", "$coin_id")
                                        )
                                        .append("amount", new Document()
                                                .append("$sum", new Document()
                                                        .append("$multiply", Arrays.asList(
                                                                "$amount",
                                                                avgPrice
                                                                )
                                                        )
                                                )
                                        )
                                ),
                        new Document()
                                .append("$project", new Document()
                                        .append("user_id", "$_id.user_id")
                                        .append("coin_id", "$_id.coin_id")
                                        .append("amount", "$amount")
                                        .append("ref_date", date)
                                )
                );

                accountCollection.aggregate(pipeline)
                        .allowDiskUse(false)
                        .forEach(new Block<Document>() {
                            @Override
                            public void apply(final Document document) {
                                document.remove("_id");
                                mongoTemplate.insert(document, "dig_account");
                            }
                        });
            }
        });

        //计算所有币种兑换成平台币的总资产
        MongoCollection<Document> digAccountCollection = mongoTemplate.getCollection("dig_account");

        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$project", new Document()
                                .append("user_id", 1.0)
                                .append("amount", 1.0)
                        ),
                new Document()
                        .append("$group", new Document()
                                .append("_id", "$user_id")
                                .append("amount", new Document()
                                        .append("$sum", "$amount")
                                )
                        ),
                new Document()
                        .append("$project", new Document()
                                .append("user_id", "$_id")
                                .append("amount", "$amount")
                                .append("date", date)
                        )
        );

        digAccountCollection.aggregate(pipeline)
                .allowDiskUse(false)
                .forEach(new Block<Document>() {
                    @Override
                    public void apply(final Document document) {
                        document.remove("_id");
                        //挖矿账户汇总
                        mongoTemplate.insert(document, "datacube_dig_account");
                    }
                });
    }

    /**
     * 计算用户成交手续费，折算成平台币的价值（应返还平台币）
     *
     * @param coinId
     */
    public void calculateRefund(Long coinId, String date) {
        mongoTemplate.remove(new Query(Criteria.where("date").is(date)), "trading_refund");

        Document query = new Document();
        query.append("buy_coin_id", coinId);
        mongoTemplate.getCollection("datacube_deal_prices").find(query).forEach(new Block<Document>() {

            @Override
            public void apply(final Document document) {
                BigDecimal avgPrice = document.get("avg_price", Decimal128.class).bigDecimalValue();
                long sellCoinId = document.getLong("sell_coin_id");

                List<? extends Bson> pipeline = Arrays.asList(
                        new Document()
                                .append("$project", new Document()
                                        .append("_id", 1)
                                        .append("user_id", 1)
                                        .append("coin_id", 1)
                                        .append("fee", 1)
                                        .append("refund", new Document()
                                                .append("$multiply", Arrays.asList(
                                                        "$fee",
                                                        avgPrice
                                                        )
                                                )
                                        )
                                ),
                        new Document()
                                .append("$match", new Document()
                                        .append("coin_id", sellCoinId)
                                        .append("date", date)
                                )
                );
                mongoTemplate.getCollection("datacube_trading_fee").aggregate(pipeline)
                        .allowDiskUse(false)
                        .forEach(new Block<Document>() {
                            @Override
                            public void apply(final Document document) {
                                long userId = document.getLong("user_id");
                                long coinId = document.getLong("coin_id");
                                BigDecimal fee = document.get("fee", Decimal128.class).bigDecimalValue();
                                BigDecimal refund = document.get("refund", Decimal128.class).bigDecimalValue();

                                Document tr = new Document();
                                tr.put("user_id", userId);
                                tr.put("coin_id", coinId);
                                tr.put("fee", fee);
                                tr.put("refund", refund);
                                tr.put("date", date);
                                mongoTemplate.insert(tr, "trading_refund");
                            }
                        });
            }
        });
    }

    /**
     * 计算用户所有币种成交手续费
     */
    public void calculateTotalRefund(String date) {
        mongoTemplate.remove(new Query(Criteria.where("date").is(date)), "trading_refund_total");

        MongoCollection<Document> tradingRefundCollection = mongoTemplate.getCollection("trading_refund");

        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$project", new Document()
                                .append("user_id", 1.0)
                                .append("refund", 1.0)
                        ),
                new Document()
                        .append("$group", new Document()
                                .append("_id", "$user_id")
                                .append("amount", new Document()
                                        .append("$sum", "$refund")
                                )
                        ),
                new Document()
                        .append("$project", new Document()
                                .append("user_id", "$_id")
                                .append("amount", "$amount")
                        )
        );

        tradingRefundCollection.aggregate(pipeline)
                .allowDiskUse(false)
                .forEach(new Block<Document>() {
                    @Override
                    public void apply(final Document document) {
                        long userId = document.getLong("user_id");
                        //应返还
                        BigDecimal returned = document.get("amount", Decimal128.class).bigDecimalValue();
                        Document query = new Document();
                        query.put("user_id", userId);
                        query.put("date", date);
                        Document mat = mongoTemplate.getCollection("datacube_dig_account").find(query).first();

                        //总资产
                        BigDecimal amount = mat.get("amount", Decimal128.class).bigDecimalValue();
                        BigDecimal ceiling = amount.multiply(new BigDecimal(0.2));
                        //实际返还
                        BigDecimal act = returned;
                        if (returned.compareTo(ceiling) > 0) {
                            act = ceiling;
                        }

                        Document trt = new Document();
                        trt.put("user_id", userId);
                        trt.put("amount", amount);
                        trt.put("act", act);
                        trt.put("date", date);
                        trt.put("rt", 0);
                        mongoTemplate.insert(trt, "trading_refund_total");
                    }
                });
    }

    //平台币每日流通量
    public void calculateMiningSum(String date) {
        MongoCollection<Document> collection = mongoTemplate.getCollection("trading_refund_total");

        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$project", new Document()
                                .append("amount", 1)
                                .append("date", 1)
                        ),
                new Document()
                        .append("$match", new Document()
                                .append("date", date)
                        ),
                new Document()
                        .append("$group", new Document()
                                .append("_id", "$date")
                                .append("amount", new Document()
                                        .append("$sum", "$amount")
                                )
                        ),
                new Document()
                        .append("$project", new Document()
                                .append("amount", "$amount")
                                .append("total", new Document()
                                        .append("$multiply", Arrays.asList(
                                                "$amount",
                                                2
                                                )
                                        )
                                )
                                .append("date", "$_id")
                        ),
                new Document()
                        .append("$out", "mining_sum")
        );

        collection.aggregate(pipeline)
                .allowDiskUse(false)
                .forEach(new Block<Document>() {
                    @Override
                    public void apply(final Document document) {

                    }
                });
    }

    //平台币总流通量
    public void calculateMiningTotal() {
        MongoCollection<Document> collection = mongoTemplate.getCollection("mining_sum");

        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$project", new Document()
                                .append("amount", 1)
                                .append("total", 1)
                        ),
                new Document()
                        .append("$group", new Document()
                                .append("_id", "")
                                .append("amount", new Document()
                                        .append("$sum", "$amount")
                                )
                                .append("total", new Document()
                                        .append("$sum", "$total")
                                )
                        ),
                new Document()
                        .append("$out", "mining_sum_total")
        );

        collection.aggregate(pipeline)
                .allowDiskUse(false)
                .forEach(new Block<Document>() {
                    @Override
                    public void apply(final Document document) {

                    }
                });
    }

    /**
     * 统计邀请人数
     */
    public void countInvitation() {
        MongoCollection<Document> collection = mongoTemplate.getCollection("user");
        mongoTemplate.dropCollection("datacube_count_invitation");
        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$project", new Document()
                                .append("id", 1.0)
                                .append("direct_inviteid", 1)
                        ),
                new Document()
                        .append("$match", new Document()
                                .append("direct_inviteid", new Document()
                                        .append("$nin", Arrays.asList(
                                                new BsonNull(),
                                                ""
                                                )
                                        )
                                        .append("$exists", true)
                                )
                        ),
                new Document()
                        .append("$group", new Document()
                                .append("_id", "$direct_inviteid")
                                .append("num", new Document()
                                        .append("$sum", 1)
                                )
                        ),
                new Document()
                        .append("$project", new Document()
                                .append("user_id", "$_id")
                                .append("num", "$num")
                        )
        );

        collection.aggregate(pipeline)
                .allowDiskUse(false)
                .forEach(new Block<Document>() {
                    @Override
                    public void apply(final Document document) {
                        document.remove("_id");
                        mongoTemplate.insert(document, "datacube_count_invitation");
                    }
                });
    }

    /**
     * 计算邀请奖励
     * * 如果A邀请的用户数 > 20， 则A用户的邀请奖励为：A的直接下级每日挖矿奖励总和（用户挖矿实际得到） * 20%；
     * * 如果A邀请的用户数 <= 20，则A用户的邀请奖励为：A的直接下级每日挖矿奖励总和（用户挖矿实际得到） * 10%；
     * * 邀请奖励冻结1周，下周一可以解冻，解冻资金上线不能超过用户上周挖矿总量
     */
    public void calculateInviteRewards(String date) {
        mongoTemplate.remove(new Query(Criteria.where("date").is(date)), "invite_rewards");

        MongoCollection userCollection = mongoTemplate.getCollection("user");
        Document dquery = new Document();
        dquery.put("num", new Document("$gt", 0));
        mongoTemplate.getCollection("datacube_count_invitation").find(dquery).forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                long userId = document.getLong("user_id");

                Document query = new Document("user_id", userId);
                query.put("num", new Document("$gt", 0));
                Document datacubeCountInvitation = mongoTemplate.getCollection("datacube_count_invitation").find(query).first();
                //邀请人数
                int num = datacubeCountInvitation.getInteger("num");
                double factor = 0.1;
                if (num > 20) {
                    factor = 0.2;
                }
                List<Long> userIds = new ArrayList<>(10000);
                userCollection.find(new Document("direct_inviteid", userId)).forEach(new Block<Document>() {
                    @Override
                    public void apply(final Document document) {
                        userIds.add(document.getLong("id"));
                    }
                });
                //邀请奖励
                BigDecimal inviteRewards = countTotalMining(userIds, date).multiply(new BigDecimal(factor)).setScale(8, BigDecimal.ROUND_HALF_UP);

                Document iRDoc = new Document();
                iRDoc.put("user_id", userId);
                iRDoc.put("amount", inviteRewards);
                iRDoc.put("date", date);
                mongoTemplate.insert(iRDoc, "invite_rewards");
            }
        });
    }

    /**
     * 统计挖矿奖励总和
     *
     * @param userIds
     * @return
     */
    public BigDecimal countTotalMining(List<Long> userIds, String date) {
        MongoCollection<Document> datacubeDigAccountCollection = mongoTemplate.getCollection("datacube_dig_account");

        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$project", new Document()
                                .append("date", 1)
                                .append("user_id", 1)
                                .append("amount", 1)
                        ),
                new Document()
                        .append("$match", new Document()
                                .append("date", date)
                                .append("user_id", new Document()
                                        .append("$in", userIds
                                        )
                                )
                        ),
                new Document()
                        .append("$group", new Document()
                                .append("_id", "")
                                .append("amount", new Document()
                                        .append("$sum", "$amount")
                                )
                        )
        );

        Document document = datacubeDigAccountCollection.aggregate(pipeline)
                .allowDiskUse(false)
                .first();
        if (!Optional.ofNullable(document).isPresent() || document.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal amount = document.get("amount", Decimal128.class).bigDecimalValue();
        return amount;
    }
}