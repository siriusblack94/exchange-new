package com.blockeng.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.blockeng.api.dto.*;
import com.blockeng.api.service.AICoinService;
import com.blockeng.api.service.EntrustOrderService;
import com.blockeng.api.util.Constants;
import com.blockeng.dto.*;
import com.blockeng.feign.MarketServiceClient;
import com.blockeng.framework.http.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static com.blockeng.api.util.Constants.ONEDAY;


/**
 * @author sirius
 * @since 2018-08-15
 */
@Slf4j
@Service
public class AICoinServiceImpl implements AICoinService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private EntrustOrderService entrustOrderService;

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private MarketServiceClient marketServiceClient;


    @Override
    public Map<String,Object> tickers() {
        Map<String,Object> map = new HashMap<>();
        List<AICoinTickerDTO> aiCoinTickerDTOS = new ArrayList<>();
        List<AlltickerDTO> alltickerDTOS =new ArrayList<>();
        long start = System.currentTimeMillis();
//        log.info("开始请求marketServiceClient--markets---" + start);
        Response response = marketServiceClient.markets();
//        log.info("responseJson----"+JSONObject.toJSONString(response));
        start = System.currentTimeMillis();
//        log.info("结束请求marketServiceClient--markets---" + start);
        List<TradeAreaMarketDTO> tradeAreaMarketDTOS = (List<TradeAreaMarketDTO>) response.getData();
        String tradeAreaMarketDTOSJson = JSONObject.toJSONString(tradeAreaMarketDTOS);
//        log.info("tradeAreaMarketDTOSJson----"+tradeAreaMarketDTOSJson);
        tradeAreaMarketDTOS = JSONObject.parseArray(tradeAreaMarketDTOSJson, TradeAreaMarketDTO.class);
        for (TradeAreaMarketDTO tradeAreaMarketDTO : tradeAreaMarketDTOS) {
            for (TradeMarketDTO tradeMarketDTO : tradeAreaMarketDTO.getMarkets()) {
                AICoinTickerDTO aiCoinTickerDTO = new AICoinTickerDTO();
                AlltickerDTO alltickerDTO = new AlltickerDTO();
                String symbol = tradeMarketDTO.getName().replace("/", "_").toLowerCase();
                String redisKey = new StringBuffer(Constants.REDIS_KEY_TRADE_KLINE).append(tradeMarketDTO.getSymbol()).append(":").append(ONEDAY).toString();
//                start = System.currentTimeMillis();
//                log.info("开始请求redisTemplate-----" + start);
                List<ArrayList> jsonLineList = redisTemplate.opsForList().range(redisKey, 0L, 0L);
//                start = System.currentTimeMillis();
//                log.info("结束请求redisTemplate-----" + start);
                String depthJson = JSONObject.toJSONString(marketServiceClient.depth(tradeMarketDTO.getSymbol(), Constants.STEP).getData());
//                start = System.currentTimeMillis();
//                log.info("结束请求marketServiceClient--depth---" + start);
                DepthsDTO depth = JSONObject.parseObject(depthJson, DepthsDTO.class);
                if (jsonLineList != null&&jsonLineList.size()>0&&depth.getAsks().size()>0&&depth.getBids().size()>0) {
                    BigDecimal high = new BigDecimal(jsonLineList.get(0).get(2).toString());
                    BigDecimal low = new BigDecimal(jsonLineList.get(0).get(3).toString());
                    aiCoinTickerDTO.setSymbol(symbol)
                            .setBuy(depth.getAsks().get(0).getPrice().stripTrailingZeros().toPlainString())
                            .setHigh(high.stripTrailingZeros().toPlainString())
                            .setLast(depth.getPrice().stripTrailingZeros().toPlainString())
                            .setLow(low.stripTrailingZeros().toPlainString())
                            .setSell(depth.getBids().get(0).getPrice().stripTrailingZeros().toPlainString())
                            .setVol(tradeMarketDTO.getVolume().stripTrailingZeros().toPlainString());
                    BeanUtils.copyProperties(aiCoinTickerDTO,alltickerDTO);
//                  alltickerDTO.setSymbol(symbol)
//                            .setBuy(depth.getAsks().get(0).getPrice().stripTrailingZeros().toPlainString())
//                            .setHigh(high.stripTrailingZeros().toPlainString())
//                            .setLast(depth.getPrice().stripTrailingZeros().toPlainString())
//                            .setLow(low.stripTrailingZeros().toPlainString())
//                            .setSell(depth.getBids().get(0).getPrice().stripTrailingZeros().toPlainString())
//                            .setVol(tradeMarketDTO.getVolume().stripTrailingZeros().toPlainString())
                    alltickerDTO.setChange(""+tradeMarketDTO.getChange());
//                    log.info("aiCoinTickerDTO----"+aiCoinTickerDTO);
//                    log.info("alltickerDTO----"+alltickerDTO);
                    aiCoinTickerDTOS.add(aiCoinTickerDTO);
                    alltickerDTOS.add(alltickerDTO);

                }
            }
        }
        map.put("aiCoinTickerDTOS",aiCoinTickerDTOS);
        map.put("alltickerDTOS",alltickerDTOS);
        return map;
    }

    @Override
    public DepthsVO depth(String symbol, int size) {
        symbol = symbol.replace("_", "").toUpperCase();
        Integer scale = 0;
        try {
            scale = Integer.valueOf(marketServiceClient.getMergeType(symbol));
        } catch (NumberFormatException e) {
            log.error("---"+symbol+"交易对异常");
            e.printStackTrace();
        }
        log.info("***"+scale);
        if (scale.equals(0)) scale=8;
        BigDecimal number = new BigDecimal(Math.pow(10, scale));
        BigDecimal mod = BigDecimal.ONE.divide(number, scale, RoundingMode.HALF_UP);
        return  entrustOrderService.queryDepths(symbol, mod, size);
    }

    @Override
    public List<AICoinTradeDTO> trades(String symbol, int size) {
        List<AICoinTradeDTO>  aiCoinTradeDTOS= new ArrayList<>();
        symbol = symbol.replace("_", "").toUpperCase();
        Query query = new Query(Criteria.where("symbol").is(symbol));
        query.with(new Sort(Sort.Direction.DESC, "created")).limit(size);
        List<TradeDealOrderDTO> tradeDealOrderDTOS = mongoTemplate.find(query, TradeDealOrderDTO.class);
        for (TradeDealOrderDTO tradeDealOrderDTO : tradeDealOrderDTOS) {
            String side = tradeDealOrderDTO.getType()==1? "buy":"sell";
            AICoinTradeDTO aiCoinTradeDTO = new AICoinTradeDTO();
            aiCoinTradeDTO.setAmount(tradeDealOrderDTO.getVolume())
                    .setPrice(tradeDealOrderDTO.getPrice())
                    .setSide(side).setTimestamp(String.valueOf(tradeDealOrderDTO.getTime().getTime()));
            aiCoinTradeDTOS.add(aiCoinTradeDTO);
        }
        return aiCoinTradeDTOS;
    }

    @Override
    public List<Object> klineOneMin(String symbol, String type, int size) {

        symbol = symbol.replace("_", "").toUpperCase();
            // redis key
        String redisKey = new StringBuffer(Constants.REDIS_KEY_TRADE_KLINE).append(symbol).append(":").append(type).toString();
        List<Object>  list = redisTemplate.opsForList().range(redisKey, 0, size - 1);
        Collections.reverse(list);
        return list;
    }

    @Override
    public List<AICoinExchangeInfoDTO> exchangeInfo() {
        List<AICoinExchangeInfoDTO> list = new ArrayList<>();
        List<MarketDTO> tradeMarkets = marketServiceClient.tradeMarkets();
        for (MarketDTO marketDTO : tradeMarkets) {
            AICoinExchangeInfoDTO exchangeInfoDTO = new AICoinExchangeInfoDTO();
            String name = marketDTO.getName();
            String exchangeInfoSymbol = name.replace("/", "_");
            String[] exchangeInfoSymbols = exchangeInfoSymbol.split("_");
            exchangeInfoDTO.setSymbol(exchangeInfoSymbol.toLowerCase());
            exchangeInfoDTO.setStatus("trading");
            exchangeInfoDTO.setBaseAsset(exchangeInfoSymbols[0].toLowerCase());
            exchangeInfoDTO.setBaseAssetPrecision(marketDTO.getNumScale());
            exchangeInfoDTO.setQuoteAsset(exchangeInfoSymbols[1].toLowerCase());
            exchangeInfoDTO.setQuoteAssetPrecision(marketDTO.getPriceScale());
            list.add(exchangeInfoDTO);
        }
        return list;
    }
}
