package com.blockeng.boss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.boss.entity.Account;
import com.blockeng.boss.entity.AccountDetail;
import com.blockeng.boss.mapper.AccountMapper;
import com.blockeng.boss.mapper.BossMapper;
import com.blockeng.boss.service.AccountDetailService;
import com.blockeng.boss.service.AccountService;
import com.blockeng.framework.enums.AmountDirection;
import com.blockeng.framework.enums.BaseStatus;
import com.blockeng.framework.enums.BusinessType;
import com.blockeng.framework.exception.AccountException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    private BossMapper bossMapper;

    @Autowired
    private AccountDetailService accountDetailService;
    /**
     * 资金转移
     *
     * @param fromUserId     转出账户
     * @param toUserId       转入账户
     * @param coinId         币种
     * @param amount         金额
     * @param buyFee         手续费
     * @param sellFee        手续费
     * @param returnAmount   成交解冻
     * @param subtractAmount 扣减资金 = amount + buyFee + returnAmount
     * @param businessType   业务类型
     * @param orderId        关联订单号
     */
    @Override
    public boolean transferBuyAmount(Long fromUserId,
                                     Long toUserId,
                                     Long coinId,
                                     BigDecimal amount,
                                     BigDecimal buyFee,
                                     BigDecimal sellFee,
                                     BigDecimal returnAmount,
                                     BigDecimal subtractAmount,
                                     BusinessType businessType,
                                     Long orderId) {
        Account fromAccount = bossMapper.queryAccount(fromUserId, coinId);
        if (fromAccount == null) {
            log.error("资金划转-资金账户异常，userId:{}, coinId:{}", fromUserId, coinId);
            throw new AccountException("资金账户异常");
        }
        Account toAccount = bossMapper.queryAccount(toUserId, coinId);
        if (toAccount == null) {
            log.error("资金划转-资金账户异常，userId:{}, coinId:{}", toUserId, coinId);
            throw new AccountException("资金账户异常");
        }
        // 扣减资金
        int count1 = bossMapper.subtractAmount(fromAccount.getId(), subtractAmount, returnAmount);
        int count2 = bossMapper.addAmount(toAccount.getId(), amount.subtract(sellFee));
        if (count1 == 1 && count2 == 1) {
            List<AccountDetail> accountDetails = new ArrayList<>(2);
            if (returnAmount.compareTo(BigDecimal.ZERO) == 1) {
                // 有成交返还，保存流水
                AccountDetail returnAccountDetail = new AccountDetail(fromUserId,
                        coinId,
                        fromAccount.getId(),
                        fromAccount.getId(),
                        orderId,
                        AmountDirection.INCOME.getType(),
                        businessType.getCode(),
                        returnAmount,
                        BigDecimal.ZERO,
                        "解冻");
                accountDetails.add(returnAccountDetail);
            }
            // 保存流水
            AccountDetail fromAccountDetail = new AccountDetail(fromUserId,
                    coinId,
                    fromAccount.getId(),
                    toAccount.getId(),
                    orderId,
                    AmountDirection.OUT.getType(),
                    businessType.getCode(),
                    amount.add(buyFee),
                    buyFee,
                    businessType.getDesc());
            // 保存流水
            AccountDetail toAccountDetail = new AccountDetail(toUserId,
                    coinId,
                    toAccount.getId(),
                    fromAccount.getId(),
                    orderId,
                    AmountDirection.INCOME.getType(),
                    businessType.getCode(),
                    amount.subtract(sellFee),
                    sellFee,
                    businessType.getDesc());
            accountDetails.add(fromAccountDetail);
            accountDetails.add(toAccountDetail);
            bossMapper.batchAddAccountDetail(accountDetails);
            return true;
        }
        log.error("资金划转失败，orderId:{}, fromUserId:{}, toUserId:{}, coinId:{}, amount:{}, buyFee:{}, sellFee:{}, businessType:{}",
                orderId, fromUserId, toUserId, coinId, amount, buyFee, sellFee, businessType.getCode());
        return false;
    }

    @Override
    public boolean transferSellAmount(Long fromUserId,
                                      Long toUserId,
                                      Long coinId,
                                      BigDecimal amount,
                                      BusinessType businessType,
                                      Long orderId) {
        Account fromAccount = bossMapper.queryAccount(fromUserId, coinId);
        if (fromAccount == null) {
            log.error("资金划转-资金账户异常，userId:{}, coinId:{}", fromUserId, coinId);
            throw new AccountException("资金账户异常");
        }
        Account toAccount = bossMapper.queryAccount(toUserId, coinId);
        if (toAccount == null) {
            log.error("资金划转-资金账户异常，userId:{}, coinId:{}", toUserId, coinId);
            throw new AccountException("资金账户异常");
        }
        // 扣减资金
        int count1 = bossMapper.subtractAmount(fromAccount.getId(), amount, BigDecimal.ZERO);
        int count2 = bossMapper.addAmount(toAccount.getId(), amount);
        if (count1 > 0 && count2 > 0) {
            List<AccountDetail> accountDetails = new ArrayList<>(2);
            // 保存流水
            AccountDetail fromAccountDetail = new AccountDetail(fromUserId,
                    coinId,
                    fromAccount.getId(),
                    toAccount.getId(),
                    orderId,
                    AmountDirection.OUT.getType(),
                    businessType.getCode(),
                    amount,
                    BigDecimal.ZERO,
                    businessType.getDesc());
            // 保存流水
            AccountDetail toAccountDetail = new AccountDetail(toUserId,
                    coinId,
                    toAccount.getId(),
                    fromAccount.getId(),
                    orderId,
                    AmountDirection.INCOME.getType(),
                    businessType.getCode(),
                    amount,
                    BigDecimal.ZERO,
                    businessType.getDesc());
            accountDetails.add(fromAccountDetail);
            accountDetails.add(toAccountDetail);
            bossMapper.batchAddAccountDetail(accountDetails);
            return true;
        }
        log.error("资金划转，orderId:{}, fromUserId:{}, toUserId:{}, coinId:{}, amount:{}, businessType:{}",
                orderId, fromUserId, toUserId, coinId, amount, businessType.getCode());
        throw new AccountException("资金划转失败");
    }

    @Override
    public boolean unlockAmount(Long userId, long coinId, BigDecimal amount, BusinessType businessType, Long orderId) {
        Account account = this.queryByUserIdAndCoinId(userId, coinId);
        if (account == null) {
            log.error("解冻资金-资金账户异常，userId:{}, coinId:{}", userId, coinId);
            throw new AccountException("资金账户异常");
        }
        if (bossMapper.unlockAmount(account.getId(), amount) > 0) {
            // 保存流水
            AccountDetail accountDetail = new AccountDetail(userId,
                    coinId,
                    account.getId(),
                    account.getId(),
                    orderId,
                    AmountDirection.INCOME.getType(),
                    businessType.getCode(),
                    amount,
                    BigDecimal.ZERO,
                    "解冻");
            accountDetailService.insert(accountDetail);
            log.error("UnlockAmount Success.解冻资金成功，orderId:{}, userId:{}, coinId:{}, unlockAmount:{}, businessType:{}",
                    orderId, userId, coinId, amount, businessType.getCode());
            return true;
        }
        log.error("UnlockAmount Error.解冻资金失败，orderId:{}, userId:{}, coinId:{}, unlockAmount:{}, businessType:{}",
                orderId, userId, coinId, amount, businessType.getCode());
        throw new AccountException("解冻资金失败");
    }

    /**
     * 根据用户ID和币种名称查询资金账户
     *
     * @param userId 用户ID
     * @param coinId 币种ID
     * @return
     */
    @Override
    public Account queryByUserIdAndCoinId(long userId, long coinId) {
        QueryWrapper<Account> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .eq("coin_id", coinId)
                .eq("status", BaseStatus.EFFECTIVE.getCode());
        return super.selectOne(wrapper);
    }
}
