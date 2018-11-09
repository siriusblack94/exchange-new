package com.blockeng.wallet.bitcoin.service;

import com.baomidou.mybatisplus.service.IService;
import com.blockeng.wallet.entity.AddressPool;

/**
 * <p>
 * 用户的地址池 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface CoinBtcAddressPoolService extends IService<AddressPool> {

    void createAddress();
}
