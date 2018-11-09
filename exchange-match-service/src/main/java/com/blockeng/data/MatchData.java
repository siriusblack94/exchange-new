package com.blockeng.data;

import com.blockeng.model.Order;
import com.blockeng.model.Tx;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/6/21 上午12:37
 * @Modified by: Chen Long
 */
public class MatchData {

    /**
     * 待撮合队列
     */
    public static BlockingQueue<Order> queue = new LinkedBlockingDeque<>();

    public static Map<String, MarketData> marketMap = new HashMap<>();

    public static Map<Long, Order> orderMap = new HashMap<>();

    /**
     * 构建成交订单
     *
     * @param buyOrder  委托买单
     * @param sellOrder 委托卖单
     * @param type      成交类型
     * @param volume    成交数量
     * @param price     成交价
     * @return
     */
    public static Tx generateTx(Order buyOrder, Order sellOrder, int type, BigDecimal volume, BigDecimal price) {
        return new Tx()
                .setMarketId(buyOrder.getMarketId())
                .setSymbol(buyOrder.getSymbol())
                .setMarketName(buyOrder.getMarketName())
                .setMarketType(buyOrder.getMarketType())
                .setTradeType(type)
                .setBuyOrderId(buyOrder.getId())
                .setBuyCoinId(buyOrder.getCoinId())
                .setBuyUserId(buyOrder.getUserId())
                .setBuyPrice(buyOrder.getPrice())
                .setBuyVolume(buyOrder.getVolume())
                .setBuyFeeRate(buyOrder.getFeeRate())
                .setSellOrderId(sellOrder.getId())
                .setSellCoinId(sellOrder.getCoinId())
                .setSellUserId(sellOrder.getUserId())
                .setSellPrice(sellOrder.getPrice())
                .setSellVolume(sellOrder.getVolume())
                .setSellFeeRate(sellOrder.getFeeRate())
                .setVol(volume)
                .setAmount(price.multiply(volume).setScale(8, RoundingMode.HALF_UP))
                .setPrice(price)
                .setCreated(new DateTime().toDate());
    }
}
