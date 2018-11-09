package com.blockeng.wallet.service;

import com.baomidou.mybatisplus.service.IService;
import com.blockeng.wallet.entity.AdminAddress;

/**
 * <p>
 * 平台归账手续费等账户 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface AdminAddressService extends IService<AdminAddress> {

    //@Cached(expire = 120, cacheType = CacheType.LOCAL)
    AdminAddress queryAdminAccount(String type, int status);

    //@Cached(expire = 120, cacheType = CacheType.LOCAL)
    AdminAddress queryAdminAccount(Long coinId, int status);


}
