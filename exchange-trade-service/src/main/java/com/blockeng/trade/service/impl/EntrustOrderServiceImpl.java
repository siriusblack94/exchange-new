package com.blockeng.trade.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.framework.constants.Constant;
import com.blockeng.framework.dto.CreateKLineDTO;
import com.blockeng.framework.dto.MatchDTO;
import com.blockeng.framework.dto.MessagePayload;
import com.blockeng.framework.enums.AmountDirection;
import com.blockeng.framework.enums.BusinessType;
import com.blockeng.framework.enums.OrderStatus;
import com.blockeng.framework.enums.OrderType;
import com.blockeng.framework.exception.AccountException;
import com.blockeng.framework.exception.GlobalDefaultException;
import com.blockeng.framework.utils.DateUtil;
import com.blockeng.framework.utils.GsonUtil;
import com.blockeng.trade.dto.CreateTradeOrderDTO;
import com.blockeng.trade.dto.EntrustOrderMatchDTO;
import com.blockeng.trade.dto.TradeDealDTO;
import com.blockeng.trade.entity.*;
import com.blockeng.trade.mapper.EntrustOrderMapper;
import com.blockeng.trade.service.*;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 委托订单业务接口实现类
 * @Author: Chen Long
 * @Date: Created in 2018/7/18 下午10:22
 * @Modified by: Chen Long
 */
@Service
@Slf4j
@Transactional
public class EntrustOrderServiceImpl extends ServiceImpl<EntrustOrderMapper, EntrustOrder> implements EntrustOrderService, Constant {

    @Autowired
    private ConfigService configService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private MarketService marketService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private TurnoverOrderService turnoverOrderService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AccountDetailService accountDetailService;

    /**
     * 委托下单
     *
     * @param market 交易市场
     * @param order  委托下单参数
     * @param userId 当前登录用户
     * @return
     */
    @Override
    public EntrustOrder createEntrustOrder(Market market, CreateTradeOrderDTO order, Long userId) {
        // 交易开关配置
        Config config = configService.queryByTypeAndCode(CONFIG_TYPE_SYSTEM, CONFIG_TRADE_STATUS);
        if (config == null || StringUtils.isEmpty(config.getValue()) || config.getValue().equals("0")) {
            log.error("交易服务暂停");
            throw new GlobalDefaultException(50001);
        }
        if (!DateUtil.isTradeTime(market.getTradeTime(), market.getTradeWeek())) {
            log.error("不在交易时间范围内");
            throw new GlobalDefaultException(50028);
        }
        BigDecimal price = order.getPrice().setScale(market.getPriceScale(), RoundingMode.DOWN);
        BigDecimal volume = order.getVolume().setScale(market.getNumScale(), RoundingMode.DOWN);
        // 预计成交额
        BigDecimal amount = price.multiply(volume);
        // 委托数量校验
        if (market.getNumMin().compareTo(BigDecimal.ZERO) == 1
                && market.getNumMin().compareTo(order.getVolume()) > 0) {
            log.error("单笔最小委托量: " + market.getNumMin() + "，当前委托量: " + order.getVolume());
            throw new GlobalDefaultException(50003);
        }
        // 委托数量校验
        if (market.getNumMax().compareTo(BigDecimal.ZERO) == 1 &&
                market.getNumMax().compareTo(order.getVolume()) < 0) {
            log.error("单笔最大委托量: " + market.getNumMax() + "，当前委托量: " + order.getVolume());
            throw new GlobalDefaultException(50004);
        }
        // 成交额校验
        if (market.getTradeMin().compareTo(BigDecimal.ZERO) == 1 &&
                market.getTradeMin().compareTo(amount) > 0) {
            log.error("单笔最小委托交易额: " + market.getTradeMin() + "，当前委托预计成交额: " + amount);
            throw new GlobalDefaultException(50005);
        }
        // 成交额校验
        if (market.getTradeMax().compareTo(BigDecimal.ZERO) == 1 &&
                market.getTradeMax().compareTo(amount) < 0) {
            log.error("单笔最大委托交易额: " + market.getTradeMax() + "，当前委托预计成交额: " + amount);
            throw new GlobalDefaultException(50006);
        }
        // 交易手续费比例
        BigDecimal feeRate = order.getType() == OrderType.BUY.getCode() ? market.getFeeBuy() : market.getFeeSell();
        // 交易费用 = 订单金额 * 交易手续费
        BigDecimal fee = amount.multiply(feeRate);
        // 冻结资金（包含手续费)
        BigDecimal freezeAmount = order.getVolume();
        if (order.getType() == OrderType.BUY.getCode()) {
            freezeAmount = price.multiply(volume).multiply(BigDecimal.ONE.add(feeRate));
        }
        // 创建委托订单
        EntrustOrder entrustOrder = new EntrustOrder();
        entrustOrder.setMarketId(market.getId())            // 交易对ID
                .setUserId(userId)                          // 用户ID
                .setMarketName(market.getName())            // 交易对名称
                .setSymbol(order.getSymbol())               // 交易对标识符
                .setPrice(price)                            // 价格
                .setType(order.getType())                   // 买卖类型
                .setVolume(volume)                          // 委托量
                .setAmount(amount)                          // 预计成交额
                .setFeeRate(feeRate)                        // 手续费费率
                .setFee(fee)                                // 手续费
                .setFreeze(freezeAmount)                    // 冻结资金
                .setDeal(BigDecimal.ZERO)                   // 已成交量
                .setStatus(OrderStatus.PENDING.getCode())   // 状态
                .setCreated(new Date());                    // 创建时间
        baseMapper.insert(entrustOrder);
        // 订单号
        long orderId = entrustOrder.getId();
        try {
            if (order.getType() == OrderType.BUY.getCode()) {
                accountService.lockAmount(userId, market.getBuyCoinId(), freezeAmount, BusinessType.TRADE_CREATE, orderId);
            } else {
                // 委托卖单冻结
                accountService.lockAmount(userId, market.getSellCoinId(), freezeAmount, BusinessType.TRADE_CREATE, orderId);
            }
        } catch (AccountException e) {
            log.error("委托下单失败：{}", e.getMessage());
            throw new GlobalDefaultException(50027);
        }
        return entrustOrder;
    }

    /**
     * 币币交易撤销委托
     *
     * @param cancelOrder 待撤销委托订单
     */
    @Override
    @Transactional
    public boolean cancelEntrustOrder(EntrustOrderMatchDTO cancelOrder) throws GlobalDefaultException {
        EntrustOrder order = baseMapper.selectById(cancelOrder.getId());
        if (order != null) {
            if (order.getStatus() == OrderStatus.PENDING.getCode()) {
                Market market = marketService.queryBySymbol(order.getSymbol());
                int count = baseMapper.cancelEntrustOrder(order.getId());
                if (count == 1) {
                  BigDecimal unlockAmount = cancelOrder.getVolume();
                    //解冻资金
                    //volume=deal+freeze
                    if (cancelOrder.getType() == OrderType.BUY.getCode()) {
                        // 买单解冻资金
                        BigDecimal price = cancelOrder.getPrice();
                        BigDecimal feeRate = cancelOrder.getFeeRate();
                        unlockAmount = price.multiply(cancelOrder.getVolume()).multiply(BigDecimal.ONE.add(feeRate));
                    }
                    long coinId = order.getType() == OrderType.BUY.getCode() ? market.getBuyCoinId() : market.getSellCoinId();
                    try {
                        accountService.unlockAmount(order.getUserId(), coinId, unlockAmount, BusinessType.TRADE_CANCEL, cancelOrder.getId());
                        // 撤销委托，更新MongoDB
                        Query query = new Query(Criteria.where("_id").is(cancelOrder.getId()));
                        mongoTemplate.remove(query, "entrust_order");
                        // 推送用户未成交委托
                        JSONObject body = new JSONObject();
                        body.put("symbol", market.getSymbol());
                        MessagePayload messagePayload = new MessagePayload();
                        messagePayload.setUserId(String.valueOf(order.getUserId()));
                        messagePayload.setChannel(CH_ORDER_PENDING);
                        messagePayload.setBody(body.toJSONString());
                        kafkaTemplate.send(CHANNEL_SENDTO_USER, new Gson().toJson(messagePayload));
                        messagePayload = new MessagePayload();
                        messagePayload.setUserId(String.valueOf(order.getUserId()));
                        messagePayload.setChannel(CH_ORDER_FINISHED);
                        messagePayload.setBody(body.toJSONString());
                        kafkaTemplate.send(CHANNEL_SENDTO_USER, new Gson().toJson(messagePayload));
                        return true;
                    } catch (AccountException e) {
                        log.error("撤单失败，解冻资金账户失败，orderId：{}", cancelOrder.getId());
                        throw new GlobalDefaultException(50019);
                    }
                }
            }
        }
        return false;
    }

    /**
     * 机器人刷单接口（自成交）
     *
     * @param market 交易市场
     * @param order  创建订单请求参数
     * @param userId 用户ID
     */
    @Override
    public void createOrder(Market market, TradeDealDTO order, Long userId) {
        Config config = configService.queryByTypeAndCode(CONFIG_TYPE_SYSTEM, "ROBOT_TRADE_FEE");
        boolean feeFlag = true;
        if (config != null && "0".equals(config.getValue())) {
            // 收取手续费
            feeFlag = false;
        }
        BigDecimal price = order.getPrice().setScale(market.getPriceScale(), RoundingMode.DOWN);
        BigDecimal volume = order.getVolume().setScale(market.getNumScale(), RoundingMode.DOWN);
        BigDecimal amount = price.multiply(volume);
        // 委托数量校验
        if (market.getNumMin().compareTo(BigDecimal.ZERO) == 1
                && market.getNumMin().compareTo(order.getVolume()) > 0) {
            log.error("单笔最小委托量: " + market.getNumMin() + "，当前委托量: " + order.getVolume());
            throw new GlobalDefaultException(50003);
        }
        // 委托数量校验
        if (market.getNumMax().compareTo(BigDecimal.ZERO) == 1 &&
                market.getNumMax().compareTo(order.getVolume()) < 0) {
            log.error("单笔最大委托量: " + market.getNumMax() + "，当前委托量: " + order.getVolume());
            throw new GlobalDefaultException(50004);
        }
        // 成交额校验
        if (market.getTradeMin().compareTo(BigDecimal.ZERO) == 1 &&
                market.getTradeMin().compareTo(amount) > 0) {
            log.error("单笔最小委托交易额: " + market.getTradeMin() + "，当前委托预计成交额: " + amount);
            throw new GlobalDefaultException(50005);
        }
        // 成交额校验
        if (market.getTradeMax().compareTo(BigDecimal.ZERO) == 1 &&
                market.getTradeMax().compareTo(amount) < 0) {
            log.error("单笔最大委托交易额: " + market.getTradeMax() + "，当前委托预计成交额: " + amount);
            throw new GlobalDefaultException(50006);
        }

        Account buyAccount = accountService.queryByUserIdAndCoinId(userId, market.getBuyCoinId());
        Account sellAccount = accountService.queryByUserIdAndCoinId(userId, market.getSellCoinId());
        //检查用户购买帐户余额是否充足
        BigDecimal buyAmount = buyAccount.getBalanceAmount();
        BigDecimal amountAddFee = amount.add(amount.multiply(market.getFeeBuy())).add(amount.multiply(market.getFeeSell()));
        if(amountAddFee.compareTo(buyAmount)>=0 ){
            log.error("自成交接口，买方帐户余额不足："+buyAccount.getBalanceAmount()+"--余额需要大于："+amountAddFee +"--用户id："+userId+"--帐户id："+buyAccount.getId());
            throw new GlobalDefaultException(50008);
        }
        //检查用户的售出帐户余额是否充足
        BigDecimal sellVolume = sellAccount.getBalanceAmount();
        if( volume.compareTo(sellVolume)>=0 ){
            log.error("自成交接口，卖方帐户余额不足："+sellAccount.getBalanceAmount()+"--余额需要大于："+sellVolume +"--用户id："+userId+"--帐户id："+sellAccount.getId());
            throw new GlobalDefaultException(50009);
        }
        // 交易手续费校验
        BigDecimal amountFee = amount.multiply(market.getFeeBuy()).add(amount.multiply(market.getFeeSell()));
        if( amountFee.compareTo(buyAccount.getBalanceAmount())>=0 ){
            log.error("交易手续费不足，帐户余额："+buyAccount.getBalanceAmount()+"--手续费需要："+amountFee +"--用户id："+userId);
            throw new GlobalDefaultException(50007);
        }

        List<EntrustOrder> orders = new ArrayList<>(2);
        // 创建已成交的买单
        EntrustOrder buyOrder = this.generateEntrustOrder(market, OrderType.BUY, price, volume, userId, feeFlag);
        // 创建已成交的买单
        EntrustOrder sellOrder = this.generateEntrustOrder(market, OrderType.SELL, price, volume, userId, feeFlag);
        orders.add(buyOrder);
        orders.add(sellOrder);
        // 保存委托订单
        super.insertBatch(orders);
        // 创建成交订单
        TurnoverOrder turnoverOrder = new TurnoverOrder();
        turnoverOrder.setMarketId(market.getId())
                .setMarketName(market.getName())            // 交易对名称
                .setSymbol(market.getSymbol())              // 交易对标识符
                .setTradeType(order.getType())              // 成交类型
                .setSellUserId(userId)                      // 卖单用户ID
                .setSellCoinId(market.getSellCoinId())      // 基础币种
                .setSellOrderId(sellOrder.getId())          // 卖单委托订单号
                .setSellPrice(price)                        // 卖单委托价格
                .setSellVolume(volume)                      // 卖单委托数量
                .setSellFeeRate(sellOrder.getFeeRate())     // 卖单手续费费率
                .setBuyUserId(userId)                       // 买单用户ID
                .setBuyCoinId(market.getBuyCoinId())        // 报价币种
                .setBuyOrderId(buyOrder.getId())            // 委托买单ID
                .setBuyPrice(price)                         // 买单委托价格
                .setBuyVolume(volume)                       // 买单委托数量
                .setBuyFeeRate(buyOrder.getFeeRate())       // 买单手续费费率
                .setDealBuyFee(buyOrder.getFee())           // 成交买单手续费
                .setDealBuyFeeRate(buyOrder.getFeeRate())   // 成交买单手续费率
                .setDealSellFee(sellOrder.getFee())         // 成交卖单手续费
                .setDealSellFeeRate(sellOrder.getFeeRate()) // 成交卖单手续费率
                .setVolume(volume)                          // 成交数量
                .setPrice(price)                            // 成交价格
                .setAmount(amount)                          // 成交金额
                .setStatus(OrderStatus.DEAL.getCode())      // 状态
                .setOrderId(sellOrder.getId())              // 主动成交订单号
                .setLastUpdateTime(new Date())              // 更新时间
                .setCreated(new Date());                    // 创建时间
        if (order.getType() == OrderType.BUY.getCode()) {
            turnoverOrder.setOrderId(buyOrder.getId());
        }
        turnoverOrderService.insert(turnoverOrder);
        // 保存最新成交订单
        mongoTemplate.save(turnoverOrder);
        System.out.println("system.out----" + GsonUtil.toJson(turnoverOrder));

        //log.info("--12345--info-" + GsonUtil.toJson(turnoverOrder));
        rabbitTemplate.convertAndSend("calc.account.tx.sum", GsonUtil.toJson(turnoverOrder));

        if (feeFlag) {
            // 扣减手续费
            accountService.subtractAmount(buyAccount.getId(), sellOrder.getFee().add(buyOrder.getFee()));
        }
        List<AccountDetail> accountDetails = new ArrayList<>(6);
        // 买单冻结流水
        AccountDetail buyFreezeDetail = new AccountDetail(userId, market.getBuyCoinId(), buyAccount.getId(), buyAccount.getId(),
                buyOrder.getId(), AmountDirection.OUT.getType(), BusinessType.TRADE_CREATE.getCode(),
                amount.add(buyOrder.getFee()), BigDecimal.ZERO, "冻结");
        // 卖单冻结流水
        AccountDetail sellFreezeDetail = new AccountDetail(userId, market.getSellCoinId(), sellAccount.getId(), sellAccount.getId(),
                sellOrder.getId(), AmountDirection.OUT.getType(), BusinessType.TRADE_CREATE.getCode(),
                volume, BigDecimal.ZERO, "冻结");
        // 买单成交扣减流水
        AccountDetail buySubtractDealDetail = new AccountDetail(userId, market.getBuyCoinId(), buyAccount.getId(), buyAccount.getId(),
                turnoverOrder.getId(), AmountDirection.OUT.getType(), BusinessType.TRADE_DEAL.getCode(),
                amount.add(buyOrder.getFee()), buyOrder.getFee(), BusinessType.TRADE_DEAL.getDesc());
        // 卖单成交扣减流水
        AccountDetail sellSubtractDealDetail = new AccountDetail(userId, market.getSellCoinId(), sellAccount.getId(), sellAccount.getId(),
                turnoverOrder.getId(), AmountDirection.OUT.getType(), BusinessType.TRADE_DEAL.getCode(),
                volume, BigDecimal.ZERO, BusinessType.TRADE_DEAL.getDesc());
        // 买单成交增加流水
        AccountDetail buyAddDealDetail = new AccountDetail(userId, market.getSellCoinId(), buyAccount.getId(), buyAccount.getId(),
                turnoverOrder.getId(), AmountDirection.INCOME.getType(), BusinessType.TRADE_DEAL.getCode(),
                volume, BigDecimal.ZERO, BusinessType.TRADE_DEAL.getDesc());
        // 卖单成交增加流水
        AccountDetail sellAddDealDetail = new AccountDetail(userId, market.getBuyCoinId(), sellAccount.getId(), sellAccount.getId(),
                turnoverOrder.getId(), AmountDirection.INCOME.getType(), BusinessType.TRADE_DEAL.getCode(),
                amount.subtract(sellOrder.getFee()), sellOrder.getFee(), BusinessType.TRADE_DEAL.getDesc());
        // 保存交易流水
        accountDetails.add(buyFreezeDetail);
        accountDetails.add(sellFreezeDetail);
        accountDetails.add(buySubtractDealDetail);
        accountDetails.add(sellSubtractDealDetail);
        accountDetails.add(buyAddDealDetail);
        accountDetails.add(sellAddDealDetail);
        // 保存资金流水
        accountDetailService.insertBatch(accountDetails);
        // 生成K线
        CreateKLineDTO createKLine = new CreateKLineDTO()
                .setSymbol(order.getSymbol())
                .setVolume(volume)
                .setPrice(sellOrder.getPrice());
        rabbitTemplate.convertAndSend("marketdata.kline", GsonUtil.toJson(createKLine));
        // 更新交易对行情
        MatchDTO matchDTO = new MatchDTO()
                .setSymbol(market.getSymbol())
                .setBuyUserId(userId)
                .setSellUserId(userId)
                .setPrice(price);
        kafkaTemplate.send("ticker", new Gson().toJson(matchDTO));
    }

    @Override
    public void startCancel(Long orderId) {
        this.baseMapper.startCancel(orderId);
    }

    /**
     * @param market    交易对
     * @param orderType 订单类型
     * @param price     价格
     * @param volume    数量
     * @param userId    用户ID
     * @param feeFlag   是否收取手续费
     * @return
     */
    private EntrustOrder generateEntrustOrder(Market market, OrderType orderType, BigDecimal price, BigDecimal volume, Long userId, boolean feeFlag) {
        EntrustOrder entrustOrder = new EntrustOrder();
        entrustOrder.setMarketId(market.getId())            // 交易对ID
                .setUserId(userId)                          // 用户ID
                .setMarketName(market.getName())            // 交易对名称
                .setSymbol(market.getSymbol())              // 交易对标识符
                .setPrice(price)                            // 价格
                .setType(orderType.getCode())               // 买卖类型
                .setVolume(volume)                          // 委托量
                .setAmount(price.multiply(volume))          // 预计成交额
                .setFeeRate(BigDecimal.ZERO)                // 手续费费率
                .setFee(BigDecimal.ZERO)                    // 手续费
                .setFreeze(BigDecimal.ZERO)                 // 冻结资金
                .setDeal(volume)                            // 已成交量
                .setStatus(OrderStatus.DEAL.getCode());     // 状态：已成交
        if (feeFlag) {
            if (orderType == OrderType.BUY) {
                entrustOrder.setFeeRate(market.getFeeBuy())     // 手续费费率
                        .setFee(price.multiply(volume)
                                .multiply(market.getFeeBuy())); // 手续费
            } else {
                entrustOrder.setFeeRate(market.getFeeSell())    // 手续费费率
                        .setFee(price.multiply(volume)
                                .multiply(market.getFeeSell()));// 手续费
            }
        }
        return entrustOrder;
    }
}
