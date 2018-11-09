package com.blockeng.mining.service.impl;


import com.blockeng.dto.CoinDTO;
import com.blockeng.dto.TradeMarketDTO;
import com.blockeng.feign.CoinServiceClient;
import com.blockeng.feign.ConfigServiceClient;
import com.blockeng.mining.config.Constant;
import com.blockeng.mining.service.MineHelpService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 挖矿帮助类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
@Slf4j
@Transactional
public class MineHelpServiceImpl implements MineHelpService {


    @Autowired
    private CoinServiceClient coinServiceClient;


    @Autowired
    private ConfigServiceClient configServiceClient;


    @Autowired
    private RedissonClient redisson;


    /**
     * 获取市场上所有币种的成交均价
     */
    public Map<Long, TradeMarketDTO> getCurrentMarket() {

        //获取所有币种信息
        Map<String, CoinDTO> coinMap = getAllCoin();//COIN表

        Map<Long, TradeMarketDTO> coinPriceMap = new HashMap<>();
        RMap<Long, TradeMarketDTO> marketMap = redisson.getMap("MARKET_CACHE");
        Collection<TradeMarketDTO> values = marketMap.values();//获取所有交易对信息
        for (TradeMarketDTO tradeMarketDTO : values) {
            String[] names = tradeMarketDTO.getName().toUpperCase().split("\\/");
            if (Constant.USDT.equalsIgnoreCase(names[1])) {//全部按照USDT对标价格计算    XXX/USDT
                coinPriceMap.put(coinMap.get(names[0]).getId(), tradeMarketDTO);
            }
        }
        return coinPriceMap;
    }

    @Override
    public Long getMineCoinId() {
        return Long.valueOf(configServiceClient.getConfig("Mining", "COIN_ID").getValue().toUpperCase());//获取挖矿交易id
    }

    @Override
    public String getMineCoinName() {
        return configServiceClient.getConfig("Mining", "COIN_NAME").getValue().toUpperCase();//获取挖矿交易名字
    }

    public TradeMarketDTO getCurrentMarket(Long coinId) {
        Map<Long, TradeMarketDTO> currentMarket = getCurrentMarket();
        return currentMarket.get(coinId);
    }

    public TradeMarketDTO getCurrentMarket(String name) {//获取对应的usdu的价格

        RMap<Long, TradeMarketDTO> marketMap = redisson.getMap("MARKET_CACHE");
        Collection<TradeMarketDTO> values = marketMap.values();//获取所有交易对信息
        for (TradeMarketDTO tradeMarketDTO : values) {
            String[] names = tradeMarketDTO.getName().toUpperCase().split("\\/");
            if (Constant.USDT.equalsIgnoreCase(names[1])) {//全部按照USDT对标价格计算
                if (name.equalsIgnoreCase(names[0])) {
                    return tradeMarketDTO;
                }
            }
        }
        return null;
    }

    @Override
    public BigDecimal getUsdtAmount(String name, BigDecimal amount) {
        TradeMarketDTO currentMarket = getCurrentMarket(name);
        if (null == currentMarket) {
            return amount;
        }
        BigDecimal price = currentMarket.getPrice();//usdt的价格
        return price.multiply(amount);
    }

    @Override
    public BigDecimal getCnyAmount(String name, BigDecimal amount) {
        BigDecimal usdtAmount = getUsdtAmount(name, amount);
        return usdtAmount.multiply(new BigDecimal(getUsdtToCny()));
    }

    @Override
    public TradeMarketDTO getMineCurrentMarket() {
        Long mineCoinId = Long.valueOf(configServiceClient.getConfig("Mining", "COIN_ID").getValue().toUpperCase());//获取挖矿交易id
        Map<Long, TradeMarketDTO> currentMarket = getCurrentMarket();
        TradeMarketDTO tradeMarketDTO = currentMarket.get(mineCoinId);
        if (null == tradeMarketDTO) {
            throw new RuntimeException("挖矿币种市场信息不能为空");
        }
        return tradeMarketDTO;
    }


    /**
     * 获取市场上所有币种的成交均价
     */
    public String getUsdtToCny() {
        return configServiceClient.getConfig("CNY", "USDT2CNY").getValue().toUpperCase();//获取挖矿交易id
    }


    public Map<String, CoinDTO> getAllCoin() {
        //获取所有币种信息
        List<CoinDTO> coinDTOS = coinServiceClient.allCoin();
        Map<String, CoinDTO> coinMap = new HashMap<>();
        for (CoinDTO item : coinDTOS) {
            coinMap.put(item.getName(), item);
        }
        return coinMap;
    }


}
