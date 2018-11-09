package com.blockeng.service;

import com.blockeng.dto.WithdrawAddressDTO;
import com.blockeng.entity.UserWallet;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户钱包表 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface UserWalletService extends IService<UserWallet> {

    /**
     * 添加提币地址
     *
     * @param withdrawAddress
     * @return
     */
    void addAddress(WithdrawAddressDTO withdrawAddress, Long userId);
}
