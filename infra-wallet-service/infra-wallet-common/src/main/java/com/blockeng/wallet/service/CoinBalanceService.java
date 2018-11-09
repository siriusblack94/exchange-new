package com.blockeng.wallet.service;

import com.baomidou.mybatisplus.service.IService;
import com.blockeng.wallet.entity.CoinBalance;

/**
 * <p>
 * 用户充值,当前用户充值成功之后添加数据到这个表,充值一般无手续费.当status为1的时候表示充值成功 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface CoinBalanceService extends IService<CoinBalance> {

    void checkBalance(String type);
}
