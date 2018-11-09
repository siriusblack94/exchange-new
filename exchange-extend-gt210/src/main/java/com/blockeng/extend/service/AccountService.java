package com.blockeng.extend.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.extend.entity.Account;

import java.math.BigDecimal;

/**
 * @Description: 资金结算
 * @Author: Chen Long
 * @Date: Created in 2018/7/10 上午12:09
 * @Modified by: Chen Long
 */
public interface AccountService  extends IService<Account> {
    boolean lockAmount(long accountId,
                       long userId,
                       long coinId,
                       BigDecimal amount,
                       long orderId) ;

    boolean addAmount(long accountId,
                      long userId,
                      long coinId,
                      BigDecimal amount,
                      long orderId) ;

    boolean subtractAmount(long accountId,
                           long userId,
                           long coinId,
                           BigDecimal amount,
                           long orderId);

    Account selectByUserAndCoinName(String userId, String coinName);
    Account selectByUserAndCoinId(long userId, long coinName);

    boolean unLockAmount(long accountId,
                      long userId,
                      long coinId,
                      BigDecimal amount,
                      long orderId);
}
