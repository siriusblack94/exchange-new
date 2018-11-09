package com.blockeng.core.task;

import com.blockeng.data.MarketData;
import com.blockeng.data.MatchData;
import com.blockeng.dto.CreateKLineDTO;
import com.blockeng.model.Order;
import com.blockeng.model.Tx;
import com.blockeng.util.GsonUtil;
import com.blockeng.rabbit.support.RabbitTemplateSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 撮合订单
 * @Author: Chen Long
 * @Date: Created in 2018/6/21 上午12:35
 * @Modified by: Chen Long
 */
@Component
@org.springframework.core.annotation.Order(value = 2)
@Slf4j
public class MatchTask implements CommandLineRunner {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitTemplateSupport templateSupport;

    @Override
    public void run(String... args) {
        while (true) {
            Order order = null;
            try {
                order = MatchData.queue.poll(100, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
            }
            if (order != null) {
                // 撮合委托订单
                this.match(order);
            }
        }
    }

    /**
     * 撮合订单
     *
     * @param order
     */
    public void match(Order order) {
        MarketData marketData = MatchData.marketMap.get(order.getSymbol());
        if (!Optional.ofNullable(marketData).isPresent()) {
            marketData = new MarketData();//两个权重队列
            MatchData.marketMap.put(order.getSymbol(), marketData);
        }
        // 当前成交量
        BigDecimal volume = order.getVolume();
        if (order.getStatus() == 2) { //取消单
            Order cancelOrder = null;
            boolean flag;
            if (order.getType() == 1) {
                // 买单
                flag = marketData.buyQueue.remove(order);
            } else {
                // 卖单
                flag = marketData.sellQueue.remove(order);
            }
            if (flag) {
                // 撮合队列删除成功
                cancelOrder = MatchData.orderMap.get(order.getId()); //????
                if (cancelOrder == null) {
                    cancelOrder = order;
                } else {
                    MatchData.orderMap.remove(order.getId());
                }
            }
            if (cancelOrder != null) {
                volume = cancelOrder.getVolume();
                Tx tx = MatchData.generateTx(cancelOrder, cancelOrder, cancelOrder.getType(), volume, cancelOrder.getPrice());
                tx.setCancel(true);
                templateSupport.convertAndSend("order.tx", GsonUtil.toJson(tx));
            }
            return;
        }
        // 当前单为委托买单
        if (order.getType() == 1) {
            // 如果卖单队列为空，则将委托单加入买单队列中
            if (marketData.sellQueue.size() == 0) {
                // 加入买单队列
                marketData.buyQueue.offer(order);
                log.debug("INQUEUE:{}", order);
                return;
            }
            // 将卖单队列中的订单取出进行撮合
            Order sellOrder = marketData.sellQueue.poll();
            log.debug("DEAL BUY:{} with {}", order, sellOrder);

            // 买单价格 < 小于卖单价格
            if (order.getPrice().compareTo(sellOrder.getPrice()) == -1) {
                // 不满足撮合条件直接加入买单队列中
                marketData.buyQueue.offer(order);
                marketData.sellQueue.offer(sellOrder);
                return;
            }
            if (order.getVolume().compareTo(sellOrder.getVolume()) >= 0) {
                // 买单量 > 买单量
                volume = sellOrder.getVolume();
            }
            // 推送行情
            CreateKLineDTO createKLine = new CreateKLineDTO()
                    .setSymbol(order.getSymbol())
                    .setVolume(volume)
                    .setPrice(sellOrder.getPrice());
            rabbitTemplate.convertAndSend("marketdata.kline", GsonUtil.toJson(createKLine));
            // 构造成交订单
            Tx tx = MatchData.generateTx(order, sellOrder, 1, volume, sellOrder.getPrice());
            // 异步资金清算
            templateSupport.convertAndSend("order.tx", GsonUtil.toJson(tx));

            if (order.getVolume().compareTo(sellOrder.getVolume()) == 1) {
                // 买单减去已成交量，继续撮合
                order.setVolume(order.getVolume().subtract(volume));
                // 未全部撮合完成
                MatchData.orderMap.put(order.getId(), order);
                MatchData.orderMap.remove(sellOrder.getId());
                // 继续撮合
                match(order);
            } else if (order.getVolume().compareTo(sellOrder.getVolume()) == -1) {
                // 买单量 < 卖单数量
                sellOrder.setVolume(sellOrder.getVolume().subtract(volume));
                marketData.sellQueue.remove(sellOrder);
                marketData.sellQueue.offer(sellOrder);

                log.debug("INQUEUE 2:{}", sellOrder);
                // 未全部撮合完成
                MatchData.orderMap.put(sellOrder.getId(), sellOrder);
                MatchData.orderMap.remove(order.getId());
            }
        } else { // 当前为卖单
            // 如果买单队列为空，则将委托订单加入卖单队列中
            if (marketData.buyQueue.size() == 0) {
                marketData.sellQueue.offer(order);
                log.debug("INQUEUE:{}", order);
                return;
            }
            // 从队列中取出买单进行撮合
            Order buyOrder = marketData.buyQueue.poll();

            log.debug("DEAL SELL:{} with {}", order, buyOrder);
            // 卖单价格 > 买单价格
            if (order.getPrice().compareTo(buyOrder.getPrice()) == 1) {
                // 不满足撮合条件直接进入撮合队列
                marketData.sellQueue.offer(order);
                marketData.buyQueue.offer(buyOrder);
                return;
            }
            if (order.getVolume().compareTo(buyOrder.getVolume()) >= 0) {
                // 卖单量 > 买单量
                volume = buyOrder.getVolume();
            }
            // 推送行情
            CreateKLineDTO createKLine = new CreateKLineDTO()
                    .setSymbol(order.getSymbol())
                    .setVolume(volume)
                    .setPrice(buyOrder.getPrice());
            rabbitTemplate.convertAndSend("marketdata.kline", GsonUtil.toJson(createKLine));
            // 构造成交订单
            Tx tx = MatchData.generateTx(buyOrder, order, 2, volume, buyOrder.getPrice());
            // 异步资金清算
            templateSupport.convertAndSend("order.tx", GsonUtil.toJson(tx));
            if (order.getVolume().compareTo(buyOrder.getVolume()) == 1) {
                order.setVolume(order.getVolume().subtract(volume));
                // 未全部撮合完成
                MatchData.orderMap.put(order.getId(), order);
                MatchData.orderMap.remove(buyOrder.getId());
                // 继续撮合
                match(order);
            } else if (order.getVolume().compareTo(buyOrder.getVolume()) == -1) {
                // 买单量大
                buyOrder.setVolume(buyOrder.getVolume().subtract(volume));
                marketData.buyQueue.remove(buyOrder);
                marketData.buyQueue.offer(buyOrder);

                log.debug("INQUEUE 2:{}", buyOrder);
                // 未全部撮合完成
                MatchData.orderMap.put(buyOrder.getId(), buyOrder);
                MatchData.orderMap.remove(order.getId());
            }
        }
    }
}
