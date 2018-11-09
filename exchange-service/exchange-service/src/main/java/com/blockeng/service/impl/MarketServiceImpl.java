package com.blockeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.dto.*;
import com.blockeng.entity.Coin;
import com.blockeng.entity.Config;
import com.blockeng.entity.Market;
import com.blockeng.entity.UserFavoriteMarket;
import com.blockeng.framework.constants.Constant;
import com.blockeng.framework.enums.*;
import com.blockeng.mapper.MarketMapper;
import com.blockeng.service.*;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.ConvertUtils;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * <p>
 * 交易对配置信息 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
@Slf4j
public class MarketServiceImpl extends ServiceImpl<MarketMapper, Market> implements MarketService, Constant {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private TurnoverOrderService turnoverOrderService;

    @Autowired
    private TradeAreaService tradeAreaService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private UserFavoriteMarketService userFavoriteMarketService;

    @Autowired
    private RedissonClient redisson;

    /**
     * 查询所有有效的币币交易市场
     *
     * @return
     */
    @Override
    public List<MarketDTO> queryMarkets() {
        QueryWrapper<Market> wrapper = new QueryWrapper<>();
        wrapper.eq("status", BaseStatus.EFFECTIVE.getCode())   // 状态条件
                .orderByAsc("sort");              // 排序
        List<Market> markets = baseMapper.selectList(wrapper);
        return MarketDTOMapper.INSTANCE.from(markets);
    }

    /**
     * 查询币币交易对行情
     *
     * @return
     */
    @Override
    public List<TradeAreaMarketDTO> queryTradeMarkets() {
        List<TradeAreaMarketDTO> marketData = new ArrayList<>();
        tradeAreaService.queryByType(TradeAreaType.DC_TYPE).forEach(tradeArea -> {
            TradeAreaMarketDTO tradeAreaMarket = this.queryTradeMarkets(tradeArea.getId());
            marketData.add(tradeAreaMarket);
        });
        return marketData;
    }

    /**
     * 查询币币交易对行情
     *
     * @param areaId 交易区域ID
     * @return
     */
    @Override
    public TradeAreaMarketDTO queryTradeMarkets(Long areaId) {
        TradeAreaMarketDTO tradeAreaMarket = new TradeAreaMarketDTO();
        TradeAreaDTO tradeArea = tradeAreaService.queryTradeAreaFromCache(areaId);
        tradeAreaMarket.setAreaName(tradeArea.getName());
        if (!StringUtils.isEmpty(tradeArea.getMarketIds())) {
            String[] strArr = tradeArea.getMarketIds().split(",");
            Long[] ids = (Long[]) ConvertUtils.convert(strArr, Long.class);
            Collection<TradeMarketDTO> tradeMarketList = getTradeMarketByIds(ids);
            // 排序
            List<TradeMarketDTO> tradeMarkets = new ArrayList<>(tradeMarketList);
            Collections.sort(tradeMarkets);
            tradeAreaMarket.setMarkets(tradeMarkets);

        }
        return tradeAreaMarket;
    }

    /**
     * 查询用户收藏的币币交易市场
     *
     * @param userId 用户ID
     * @return
     */
    @Override
    public List<TradeAreaMarketDTO> queryFavoriteTradeMarkets(Long userId) {
        List<TradeAreaMarketDTO> marketData = new ArrayList<>();
        TradeAreaMarketDTO tradeAreaMarket = new TradeAreaMarketDTO();
        marketData.add(tradeAreaMarket);
        tradeAreaMarket.setAreaName("自选");
        List<UserFavoriteMarket> userFavoriteMarketList = userFavoriteMarketService.queryUserFavoriteMarket(userId, MarketType.TRADE);
        int size = userFavoriteMarketList.size();
        Long[] ids = new Long[size];
        for (int i = 0; i < size; i++) {
            ids[i] = userFavoriteMarketList.get(i).getMarketId();
        }
        Collection<TradeMarketDTO> tradeMarketList = getTradeMarketByIds(ids);
        // 排序
        List<TradeMarketDTO> tradeMarkets = new ArrayList<>(tradeMarketList);
        Collections.sort(tradeMarkets);
        tradeAreaMarket.setMarkets(tradeMarkets);
        return marketData;
    }

    /**
     * 根据交易对标识符查询交易对（缓存2分钟）
     *
     * @param symbol 交易对标识符
     * @return
     */
    @Override
    public Market queryBySymbol(String symbol) {
        QueryWrapper<Market> wrapper = new QueryWrapper<>();
        wrapper.eq("symbol", symbol);
        wrapper.eq("status", BaseStatus.EFFECTIVE.getCode());
        List<Market> markets = baseMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(markets)) {
            return null;
        }
        return markets.get(0);
    }

    @Override
    public TradeMarketDTO getTradeMarketById(Long marketId) {
        RMap<Long, TradeMarketDTO> marketMap = redisson.getMap("MARKET_CACHE");
        return marketMap.get(marketId);
    }

    @Override
    public Collection<TradeMarketDTO> getTradeMarketByIds(Long[] marketIds) {
        RMap<Long, TradeMarketDTO> marketMap = redisson.getMap("MARKET_CACHE");
        return marketMap.getAll(Sets.newHashSet(marketIds)).values();
    }

    @Override
    public Collection<TradeMarketDTO> queryTradeMarketList() {
        RMap<Long, TradeMarketDTO> marketMap = redisson.getMap("MARKET_CACHE");
        return marketMap.values();
    }

    /**
     * 查询交易市场（缓存）
     *
     * @param marketId
     * @return
     */
    @Override
    public Market getMarketById(Long marketId) {
        return baseMapper.selectById(marketId);
    }

    /**
     * 计算人民币价格
     *
     * @param market 交易对
     * @return
     */
    @Override
    public BigDecimal getCnyPrice(Market market) {
        Config config = configService.queryByTypeAndCode(CONFIG_TYPE_CNY, CONFIG_CNY2USDT);

        if (config == null || StringUtils.isEmpty(config.getValue())) {
            return BigDecimal.ZERO;
        }
        BigDecimal usdt2cnyRate = new BigDecimal(config.getValue());
        long priceCoinId = market.getBuyCoinId();
        Coin priceCoin = coinService.queryById(priceCoinId);

        Config platformCoinConfig = configService.queryByTypeAndCode(CONFIG_TYPE_SYSTEM, "PLATFORM_COIN_ID");
        if (platformCoinConfig == null) {
            return BigDecimal.ZERO;
        }
        if (platformCoinConfig.getValue().equals(String.valueOf(priceCoin.getId()))) {
            // 非USDT交易对 人民币价格 = 当前价  * USDT对人民币费率
            return usdt2cnyRate.setScale(2, RoundingMode.HALF_UP);
        }
        List<MarketDTO> marketDTOList = this.queryMarkets();
        for (MarketDTO marketDTO : marketDTOList) {
            long buyCoinId = marketDTO.getBuyCoinId();
            long sellCoinId = marketDTO.getSellCoinId();
            Coin buyCoin = coinService.queryById(buyCoinId);
            if (!platformCoinConfig.getValue().equals(String.valueOf(buyCoin.getId()))) {
                continue;
            }
            if (priceCoinId != sellCoinId) {
                continue;
            }
            BigDecimal usdtPrice = turnoverOrderService.queryCurrentPrice(marketDTO.getId());

            BigDecimal result = usdtPrice.multiply(usdt2cnyRate).setScale(2, RoundingMode.HALF_UP);

            return result;
        }
        return BigDecimal.ZERO;
    }

    @Override
    public Collection<TradeMarketDTO> queryByType(MarketType trade, String marketIds) {
        String[] strArr = marketIds.split(",");
        Long[] ids = (Long[]) ConvertUtils.convert(strArr, Long.class);
        Collection<TradeMarketDTO> tradeMarketList = getTradeMarketByIds(ids);

        return tradeMarketList;
    }

    @Override
    public void refresh(Long marketId, BigDecimal price) {
        Market market = getMarketById(marketId);
        refreshLatestPrice(market, price);
        refresh24HDeal(market.getSymbol());
    }

    @Override
    public TradeMarketDTO refresh24HDeal(String symbol) {
        RMap<Long, TradeMarketDTO> marketMap = redisson.getMap("MARKET_CACHE");
        Market market = queryBySymbol(symbol);
        if (market != null) {
            TradeMarketDTO marketDTO = marketMap.get(market.getId());
            if (Optional.ofNullable(marketDTO).isPresent()) {
                // 获取24H成交数量
                TurnoverData24HDTO turnoverData24H = turnoverOrderService.query24HDealData(market.getSymbol());
                if (turnoverData24H != null) {
                    marketDTO.setVolume(turnoverData24H.getVolume())  // 24小时成交量
                            .setAmount(turnoverData24H.getAmount());    // 24小时成交额
                }
                // 获取交易对K线
                String redisKey = new StringBuffer(REDIS_KEY_TRADE_KLINE)
                        .append(market.getSymbol())
                        .append(":").append(KlineType.ONE_DAY.getValue()).toString();
                List<String> jsonLineList = redisTemplate.opsForList().range(redisKey, 0L, 0L);
                if (!CollectionUtils.isEmpty(jsonLineList)) {
                    // 已缓存的最新K线数据
                    Line line = new Line(jsonLineList.get(0));
                    // 张跌幅 = （当前价 - 前一日的收盘价） / 前一日的收盘价  * 100%；
                    // 前一日收盘价即今日开盘价
                    BigDecimal change = marketDTO.getPrice().subtract(line.getOpen())
                            .divide(line.getOpen(), 4, RoundingMode.HALF_UP)
                            .multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
                    marketDTO.setHigh(line.getHigh())
                            .setLow(line.getLow())
                            .setChange(change.doubleValue());
                    marketMap.put(market.getId(), marketDTO);
                }
            }
            return marketDTO;
        }
        return null;
    }

    /**
     * @return
     */
    @Override
    public TradeMarketDTO refreshLatestPrice(Market market, BigDecimal price) {
        RMap<Long, TradeMarketDTO> marketMap = redisson.getMap("MARKET_CACHE");
        TradeMarketDTO tradeMarket = marketMap.get(market.getId());
        if (tradeMarket == null) {
            tradeMarket = new TradeMarketDTO();
        }
        tradeMarket.setSymbol(market.getSymbol())       // 交易对标识符
                .setName(market.getName())              // 交易对名称
                .setImage(market.getImg())              // 交易对图片
                .setSort(market.getSort())              // 显示顺序
                .setBuyFeeRate(market.getFeeBuy())      // 买入手续费
                .setSellFeeRate(market.getFeeSell())    // 卖出手续费
                .setPriceScale(market.getPriceScale())  // 价格小数位数
                .setNumScale(market.getNumScale())      // 数量小数位数
                .setNumMin(market.getNumMin())          // 最小委托数量
                .setNumMax(market.getNumMax())          // 最大委托数量
                .setTradeMax(market.getTradeMax())      // 最大成交
                .setTradeMin(market.getTradeMin())      // 最小成交额
                .setPrice(market.getOpenPrice())        // 当前价
                .setCnyPrice(market.getOpenPrice())     // 当前价CNY价格
                .setCoinCnyPrice(market.getOpenPrice()) // 报价币种人民币价格
                .setHigh(market.getOpenPrice())         // 最高价
                .setLow(market.getOpenPrice())          // 最低价
                .setMergeDepth(this.getMergeDepth(market.getMergeDepth())); // 合并深度
        long priceCoinId = market.getBuyCoinId();
        Coin priceCoin = coinService.queryById(priceCoinId);
        BigDecimal coinCnyPrice = this.getCnyPrice(market);
        // 设置最新成交价，价格单位，对应CNY价格
        tradeMarket.setPrice(price)
                .setPriceUnit(priceCoin.getName())
                .setCnyPrice(coinCnyPrice.multiply(price).setScale(2, RoundingMode.HALF_UP))
                .setCoinCnyPrice(coinCnyPrice);
        marketMap.put(market.getId(), tradeMarket);
        return tradeMarket;
    }

    /**
     * 合并深度
     *
     * @return
     */
    private List<MergeDepthDTO> getMergeDepth(String mergeDepth) {
        if (StringUtils.isEmpty(mergeDepth)) {
            return null;
        }
        List<MergeDepthDTO> mergeDepthList = new ArrayList<>();
        String[] mergeDepthArray = mergeDepth.split(",");
        for (int i = 0; i < mergeDepthArray.length; i++) {
            MergeDepthDTO mergeDepthDTO = new MergeDepthDTO();
            mergeDepthList.add(mergeDepthDTO);
            mergeDepthDTO.setMergeType(DepthMergeType.getByValue(i).getCode());
            int scale = Integer.parseInt(mergeDepthArray[i]);
            BigDecimal number = new BigDecimal(Math.pow(10, scale));
            BigDecimal value = BigDecimal.ONE.divide(number, scale, RoundingMode.HALF_UP);
            mergeDepthDTO.setValue(value);
        }
        return mergeDepthList;
    }
}
