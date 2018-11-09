package com.blockeng.wallet.bitcoin.service;

import com.baomidou.mybatisplus.service.IService;
import com.blockeng.wallet.entity.CoinWithdraw;

/**
 * <p>
 * 当用户发起提币的时候,吧数据插入到该表 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface CoinBtcWithdrawService extends IService<CoinWithdraw> {

    // void transaction();

    /**
     * 提币
     */

    void transaction();
}
