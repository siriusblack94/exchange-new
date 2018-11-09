package com.blockeng.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.framework.constants.Constant;
import com.blockeng.framework.enums.AmountDirection;
import com.blockeng.framework.enums.BaseStatus;
import com.blockeng.framework.enums.BusinessType;
import com.blockeng.framework.exception.AccountException;
import com.blockeng.trade.entity.Account;
import com.blockeng.trade.entity.AccountDetail;
import com.blockeng.trade.mapper.AccountMapper;
import com.blockeng.trade.service.AccountDetailService;
import com.blockeng.trade.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * <p>
 * 用户财产记录 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
@Slf4j
@Transactional
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService, Constant {

    @Autowired
    private AccountDetailService accountDetailService;

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

    /**
     * 冻结资金
     *
     * @param userId       用户ID
     * @param coinId       币种ID
     * @param amount       冻结金额
     * @param businessType 业务类型
     * @param orderId      关联订单号
     * @return
     */
    @Override
    public boolean lockAmount(Long userId,
                              Long coinId,
                              BigDecimal amount,
                              BusinessType businessType,
                              long orderId) throws AccountException {
        Account account = this.queryByUserIdAndCoinId(userId, coinId);
        if (account == null) {
            log.error("冻结资金-资金账户异常，userId:{}, coinId:{}", userId, coinId);
            throw new AccountException("资金账户异常");
        }
        if (baseMapper.lockAmount(account.getId(), amount) > 0) {
            // 保存流水
            AccountDetail accountDetail = new AccountDetail(userId,
                    coinId,
                    account.getId(),
                    account.getId(),
                    orderId,
                    AmountDirection.OUT.getType(),
                    businessType.getCode(),
                    amount,
                    BigDecimal.ZERO,
                    "冻结");
            accountDetailService.insert(accountDetail);
            return true;
        }
        log.error("LockAmount Error.冻结资金失败，orderId:{}, userId:{}, coinId:{}, amount:{}, businessType:{}",
                orderId, userId, coinId, amount, businessType.getCode());
        throw new AccountException("可用资金不足");
    }

    /**
     * 解冻资金
     *
     * @param userId       用户ID
     * @param coinId       币种ID
     * @param amount       冻结金额
     * @param businessType 业务类型
     * @param orderId      关联订单号
     * @return
     */
    @Override
    public boolean unlockAmount(Long userId,
                                Long coinId,
                                BigDecimal amount,
                                BusinessType businessType,
                                Long orderId) throws AccountException {
        Account account = this.queryByUserIdAndCoinId(userId, coinId);
        if (account == null) {
            log.error("解冻资金-资金账户异常，userId:{}, coinId:{}", userId, coinId);
            throw new AccountException("资金账户异常");
        }
        if (baseMapper.unlockAmount(account.getId(), amount) > 0) {
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
     * 扣减资金
     *
     * @param accountId 资金账户ID
     * @param amount    扣减资金额度
     * @return
     */
    @Override
    public int subtractAmount(Long accountId, BigDecimal amount) {
        return baseMapper.subtractAmount(accountId, amount);
    }
}
