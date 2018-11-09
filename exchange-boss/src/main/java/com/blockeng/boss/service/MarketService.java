package com.blockeng.boss.service;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.blockeng.boss.entity.Market;
import com.blockeng.framework.constants.Constant;

/**
 * @Description: 交易市场业务接口
 * @Author: Chen Long
 * @Date: Created in 2018/7/19 下午3:29
 * @Modified by: Chen Long
 */
public interface MarketService extends Constant {

    /**
     * 查询交易对(缓存2分钟)
     *
     * @param symbol 交易对标识符
     * @return
     */
    @Cached(name = CACHE_KEY_MARKET, expire = 120, cacheType = CacheType.LOCAL)
    Market queryBySymbol(String symbol);
}
