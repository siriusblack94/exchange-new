package com.blockeng.service;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.dto.MarketDTO;
import com.blockeng.dto.TradeAreaMarketDTO;
import com.blockeng.dto.TradeMarketDTO;
import com.blockeng.entity.Market;
import com.blockeng.framework.constants.Constant;
import com.blockeng.framework.enums.MarketType;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 交易对配置信息 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface MarketService extends IService<Market>, Constant {

    /**
     * 按类型查询所有有效的交易市场（缓存2分钟）
     *
     * @return
     */
    @Cached(name = CACHE_KEY_MARKETS, expire = 120, cacheType = CacheType.LOCAL)
    List<MarketDTO> queryMarkets();

    /**
     * 查询币币交易对行情
     *
     * @return
     */
    List<TradeAreaMarketDTO> queryTradeMarkets();

    /**
     * 查询币币交易对行情
     *
     * @param areaId 交易区域ID
     * @return
     */
    TradeAreaMarketDTO queryTradeMarkets(Long areaId);

    /**
     * 查询用户收藏的币币交易市场
     *
     * @param userId 用户ID
     * @return
     */
    List<TradeAreaMarketDTO> queryFavoriteTradeMarkets(Long userId);

    /**
     * 根据交易对标识符查询交易对（缓存2分钟）
     *
     * @param symbol 交易对标识符
     * @return
     */
    @Cached(name = CACHE_KEY_MARKET, expire = 120, cacheType = CacheType.LOCAL)
    Market queryBySymbol(String symbol);

    /**
     * 查询交易市场（缓存120秒）
     *
     * @param marketId 交易对ID
     * @return
     */
    @Cached(name = CACHE_KEY_MARKET, expire = 120, cacheType = CacheType.LOCAL)
    Market getMarketById(Long marketId);

    TradeMarketDTO getTradeMarketById(Long marketId);

    Collection<TradeMarketDTO> getTradeMarketByIds(Long[] marketIds);

    Collection<TradeMarketDTO> queryTradeMarketList();

    /**
     * 计算人民币价格
     *
     * @param market 交易对
     * @return
     */
    BigDecimal getCnyPrice(Market market);

    /**
     * 按类型查询所有有效的交易市场（缓存2分钟）
     *
     * @param trade 交易对类型
     * @return
     */
    Collection<TradeMarketDTO> queryByType(MarketType trade, String marketIds);

    /**
     * 刷新市场行情缓存
     *
     * @param marketId 交易对ID
     */
    void refresh(Long marketId, BigDecimal price);

    TradeMarketDTO refreshLatestPrice(Market market, BigDecimal price);

    /**
     * 刷新24小时成交数据
     */
    TradeMarketDTO refresh24HDeal(String symbol);
}
