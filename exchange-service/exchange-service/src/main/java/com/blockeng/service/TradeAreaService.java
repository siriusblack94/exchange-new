package com.blockeng.service;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.blockeng.dto.TradeAreaDTO;
import com.blockeng.entity.TradeArea;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.framework.constants.Constant;
import com.blockeng.framework.enums.TradeAreaType;

import java.util.List;

/**
 * <p>
 * 交易区 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface TradeAreaService extends IService<TradeArea>, Constant {

    /**
     * 查询交易区域(缓存2分钟)
     *
     * @param areaId 交易区域ID
     * @return
     */
    @Cached(name = CACHE_KEY_TRADE_AREA, expire = 30, cacheType = CacheType.LOCAL)
    TradeAreaDTO queryTradeAreaFromCache(long areaId);

    /**
     * 根据类型查询交易区域(缓存2分钟)
     *
     * @param tradeAreaType
     * @return
     */
    @Cached(name = CACHE_KEY_TRADE_AREAS, expire = 30, cacheType = CacheType.LOCAL)
    List<TradeAreaDTO> queryByType(TradeAreaType tradeAreaType);
}
