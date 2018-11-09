package com.blockeng.wallet.ethereum.service;


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
public interface CoinEthAddressPoolService extends IService<AddressPool> {

    void createAddress();
}
