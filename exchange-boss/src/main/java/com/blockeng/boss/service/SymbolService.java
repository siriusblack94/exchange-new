package com.blockeng.boss.service;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.blockeng.boss.dto.Symbol;

import static com.blockeng.framework.constants.Constant.CACHE_KEY_MARKETS;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/7/10 上午1:48
 * @Modified by: Chen Long
 */
public interface SymbolService {

    /**
     * 查询交易对币种信息
     *
     * @param marketId 交易对ID
     * @return
     */
    @Cached(name = CACHE_KEY_MARKETS, cacheType = CacheType.LOCAL)
    Symbol queryById(Long marketId);
}
