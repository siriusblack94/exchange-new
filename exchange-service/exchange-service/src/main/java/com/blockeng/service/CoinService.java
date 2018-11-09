package com.blockeng.service;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.blockeng.entity.Coin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.framework.constants.Constant;

/**
 * <p>
 * 币种配置信息 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface CoinService extends IService<Coin>, Constant {

    /**
     * 根据主键查询币种信息
     *
     * @param coinId 币种ID
     * @return
     */
    @Cached(name = CACHE_KEY_COIN, expire = 120, cacheType = CacheType.LOCAL)
    Coin queryById(long coinId);

    /**
     * 根据类型查询币种信息
     *
     * @param type 币种类型
     * @return
     */
    @Cached(name = CACHE_KEY_COIN, expire = 120, cacheType = CacheType.LOCAL)
    Coin queryByType(String type);

    /**
     * 根据主键查询币种信息
     *
     * @param coinName 币种名称
     * @return
     */
    @Cached(name = CACHE_KEY_COIN, expire = 120, cacheType = CacheType.LOCAL)
    Coin queryByName(String coinName);
}
