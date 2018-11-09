package com.blockeng.trade.service;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.framework.constants.Constant;
import com.blockeng.trade.entity.Config;

/**
 * <p>
 * 平台配置信息 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface ConfigService extends IService<Config>, Constant {

    /**
     * 根据code查询配置信息
     *
     * @param type 代码类型
     * @param code 配置代码
     * @return
     */
    @Cached(name = CACHE_KEY_CONFIG, expire = 120, cacheType = CacheType.LOCAL)
    Config queryByTypeAndCode(String type, String code);
}
