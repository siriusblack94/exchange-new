package com.blockeng.wallet.service;

import com.baomidou.mybatisplus.service.IService;
import com.blockeng.wallet.dto.CoinAddressDTO;
import com.blockeng.wallet.dto.WalletResultDTO;
import com.blockeng.wallet.entity.UserAddress;

/**
 * <p>
 * 用户钱包地址信息 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface UserAddressService extends IService<UserAddress> {

    UserAddress getByCoinIdAndAddr(String to, Long coinId);

    UserAddress getByCoinIdAndUserId(Long userId, Long coinId);

    int selectCount(String to, long nowCoinId);


    String getAddress(CoinAddressDTO getAddressDTO);
}
