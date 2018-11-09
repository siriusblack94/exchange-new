package com.blockeng.task.service;

import com.blockeng.dto.Line;
import com.blockeng.framework.constants.Constant;
import com.blockeng.framework.dto.CreateKLineDTO;
import com.blockeng.framework.enums.KlineType;
import com.blockeng.task.util.KlineTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: 币币交易K线 Service
 * @Author: Chen Long
 * @Date: Created in 2018/5/15 下午6:06
 * @Modified by: Chen Long
 */
@Component
@Slf4j
public class TradeKLineService implements Runnable, Constant {

    /*    */

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        TradeKLineService.redisTemplate = redisTemplate;
    }

    /**
     * K线数据队列
     *//*
    public static BlockingQueue<CreateKLineDTO> queue = new LinkedBlockingDeque<>();*/

    private static StringRedisTemplate redisTemplate;

    private CreateKLineDTO klineData;

    private KlineType klineType;

    public TradeKLineService() {
    }

    public TradeKLineService(CreateKLineDTO klineData, KlineType klineType) {
        this.klineData = klineData;
        this.klineType = klineType;
    }

    @Override
    public void run() {
        generateKLine(klineData, klineType);
    }

    /**
     * 币币交易生成K线
     *
     * @param klineData 交易对开盘价
     * @param klineType K线类型
     */
    public void generateKLine(CreateKLineDTO klineData, KlineType klineType) {
        // redis key
        String redisKey = new StringBuffer(REDIS_KEY_TRADE_KLINE).append(klineData.getSymbol()).append(":").append(klineType.getValue()).toString();
        // 当前k线时间
        DateTime time = KlineTimeUtil.getKLineTime(klineType);
        Line line;
        // list 大小
        Long size = redisTemplate.opsForList().size(redisKey);
        if (size == 0L) {
            // 第一次缓存K线
            line = new Line(time, klineData.getPrice(), klineData.getVolume());
            redisTemplate.opsForList().leftPush(redisKey, line.toKline());
            return;
        }
        // 已经有缓存数据
        List<String> jsonLineList = redisTemplate.opsForList().range(redisKey, 0L, 0L);
        // 已缓存的最新K线数据
        Line historyLine = new Line(jsonLineList.get(0));
        DateTime cacheTime = historyLine.getTime();
        // 当前数据时间 > 缓存数据时间，需要生成新k线
        if (time.compareTo(cacheTime) == 1) {
            // 如果超过缓存的最大数量， 则删除最后一个元素
            if (size >= REDIS_MAX_CACHE_KLINE_SIZE) {
                redisTemplate.opsForList().rightPop(redisKey);
            }
            // 当前没有成交数据，无需修上一K线的收盘价
            if (klineData.getVolume().compareTo(BigDecimal.ZERO) == 0) {
                // 创建新k线，开、高、低、收都为上一根K线的收盘价
                line = new Line(time, historyLine.getClose(), BigDecimal.ZERO);
                redisTemplate.opsForList().leftPush(redisKey, line.toKline());
                return;
            }
            line = new Line();
            line.setTime(time)
                    .setOpen(klineData.getPrice())
                    .setClose(klineData.getPrice())
                    .setVolume(klineData.getVolume());
            if (klineData.getPrice().compareTo(historyLine.getClose()) >= 0) {
                line.setHigh(klineData.getPrice()).setLow(historyLine.getClose());
            } else {
                line.setHigh(historyLine.getClose()).setLow(klineData.getPrice());
            }
            // 更新收盘价
            historyLine.setClose(klineData.getPrice());
            redisTemplate.opsForList().set(redisKey, 0, historyLine.toKline());
            // 生成新K线数据
            redisTemplate.opsForList().leftPush(redisKey, line.toKline());
            return;
        }
        if (klineData.getVolume().compareTo(BigDecimal.ZERO) == 0) {
            // 没有成交量不需要更新K线数据
            return;
        }
        // 更新当前K线数据：最高价，最低价, 收盘价、成交量
        if (klineData.getPrice().compareTo(historyLine.getHigh()) == 1) {
            historyLine.setHigh(klineData.getPrice());
        }
        if (klineData.getPrice().compareTo(historyLine.getLow()) == -1) {
            historyLine.setLow(klineData.getPrice());
        }
        historyLine.setClose(klineData.getPrice());
        historyLine.setVolume(historyLine.getVolume().add(klineData.getVolume()));
        redisTemplate.opsForList().set(redisKey, 0, historyLine.toKline());
    }
}
