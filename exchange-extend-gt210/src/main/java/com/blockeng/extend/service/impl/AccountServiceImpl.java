package com.blockeng.extend.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.extend.entity.Account;

import com.blockeng.extend.entity.AccountDetail;
import com.blockeng.extend.mapper.AccountDetailMapper;
import com.blockeng.extend.mapper.AccountMapper;
import com.blockeng.extend.service.AccountService;
import com.blockeng.framework.enums.AmountDirection;
import com.blockeng.framework.enums.BusinessType;
import com.blockeng.framework.exception.AccountException;
import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/7/10 上午2:38
 * @Modified by: Chen Long
 */
@Service
@Slf4j
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Autowired
    AccountDetailMapper accountDetailMapper;

    /**
     * 冻结资金
     *
     * @param userId       用户ID
     * @param coinId       币种ID
     * @param amount       冻结金额

     * @param orderId      关联订单号
     * @return
     */
    @Override
    public boolean unLockAmount(long accountId,
                              long userId,
                              long coinId,
                              BigDecimal amount,
                              long orderId) {
        if (baseMapper.unLockAmount(accountId, amount) > 0) {
            // 保存流水
            AccountDetail accountDetail = new AccountDetail(userId,
                    coinId,
                    accountId,
                    accountId,
                    orderId,
                    2,
                    "synchronous",
                    amount,
                    "解冻");
            accountDetailMapper.insert(accountDetail);
            return true;
        }
        log.error("LockAmount Error.冻结资金失败，orderId:{}, userId:{}, coinId:{}, amount:{}, businessType:{}",
                orderId, userId, coinId, amount,"积分同步冻结" );
        return false;
    }

    /**
     * 冻结资金
     *
     * @param userId       用户ID
     * @param coinId       币种ID
     * @param amount       冻结金额

     * @param orderId      关联订单号
     * @return
     */
    @Override
    public boolean lockAmount(long accountId,
                              long userId,
                              long coinId,
                              BigDecimal amount,
                              long orderId) {
        if (baseMapper.lockAmount(accountId, amount) > 0) {
            // 保存流水
            AccountDetail accountDetail = new AccountDetail(userId,
                    coinId,
                    accountId,
                    accountId,
                    orderId,
                    2,
                    "synchronous",
                    amount,
                    "冻结");
            accountDetailMapper.insert(accountDetail);
            return true;
        }
        log.error("LockAmount Error.冻结资金失败，orderId:{}, userId:{}, coinId:{}, amount:{}, businessType:{}",
                orderId, userId, coinId, amount,"积分同步冻结" );
        return false;
    }
    /**
     * 增加资金
     *
     * @param userId       用户ID
     * @param coinId       币种ID
     * @param amount       冻结金额
     * @param orderId      关联订单号
     * @return
     */
    @Override
    public boolean addAmount(long accountId,
                             long userId,
                             long coinId,
                             BigDecimal amount,
                             long orderId) throws AccountException {
        if (baseMapper.addAmount(accountId, amount) > 0) {
            // 保存流水
            AccountDetail accountDetail = new AccountDetail(userId,
                    coinId,
                    accountId,
                    accountId,
                    orderId,
                    1,
                    "anti-synchronous",
                    amount,"积分赎回");
            accountDetailMapper.insert(accountDetail);
            return true;
        }
        log.error("增加资金失败，orderId:{}, userId:{}, coinId:{}, amount:{}, businessType:{}",
                orderId, userId, coinId, amount,"积分赎回");
        return false;
    }

    /**
     * 扣减资金
     *
     * @param userId       用户ID
     * @param coinId       币种ID
     * @param amount       冻结金额
     * @param orderId      关联订单号
     * @return
     */
    @Override
    public boolean subtractAmount(long accountId,
                                  long userId,
                                  long coinId,
                                  BigDecimal amount,
                                  long orderId) throws AccountException {
        if (baseMapper.subtractAmount(accountId, amount) > 0) {
            // 保存流水
            AccountDetail accountDetail = new AccountDetail(userId,
                    coinId,
                    accountId,
                    accountId,
                    orderId,
                    2,
                    "synchronous",
                    amount,"积分同步");
            accountDetailMapper.insert(accountDetail);
            return true;
        }
        log.error("扣减资金失败，orderId:{}, userId:{}, coinId:{}, amount:{}, businessType:{}",
                orderId, userId, coinId, amount,"积分同步");
        return false;
    }
    @Override
    public Account selectByUserAndCoinName(String userId, String coinName){
      return   baseMapper.selectByUserAndCoinName(userId,coinName);
    }
    @Override
    public Account selectByUserAndCoinId(long userId, long coinName){
        return   baseMapper.selectByUserAndCoinId(userId,coinName);
    }
}
