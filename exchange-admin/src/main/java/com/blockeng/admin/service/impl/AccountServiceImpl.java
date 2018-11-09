package com.blockeng.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.admin.dto.AccountDTO;
import com.blockeng.admin.entity.Account;
import com.blockeng.admin.entity.AccountDetail;
import com.blockeng.admin.entity.User;
import com.blockeng.admin.entity.WalletInfo;
import com.blockeng.admin.mapper.AccountMapper;
import com.blockeng.admin.service.AccountDetailService;
import com.blockeng.admin.service.AccountService;
import com.blockeng.admin.service.UserService;
import com.blockeng.framework.enums.AdminUserType;
import com.blockeng.framework.enums.AmountDirection;
import com.blockeng.framework.enums.BaseStatus;
import com.blockeng.framework.enums.BusinessType;
import com.blockeng.framework.exception.AccountException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户财产记录 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Service
@Slf4j
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Autowired
    private AccountDetailService accountDetailService;

    @Autowired
    private UserService userService;

    @Override
    public Page<Account> selectListPage(Page<Account> page, Wrapper<Account> wrapper) {
        wrapper = (Wrapper<Account>) SqlHelper.fillWrapper(page, wrapper);
        page.setOptimizeCountSql(true);
        page.setRecords(baseMapper.selectListPage(page, wrapper));
        return page;
    }



    @Override
    public boolean lockAmount(Long accountId, BigDecimal amount, Long orderId, BusinessType businessType) {
        Account account = selectById(accountId);
        if (null == account) {
            throw new AccountException("资金账户不存在");
        }
        if (account.getBalanceAmount().compareTo(amount) < 0) {
            throw new AccountException("资金不足");
        }
        baseMapper.lockAmount(accountId, amount);
        AccountDetail accountDetail = new AccountDetail(account.getUserId(), account.getCoinId(), account.getId(), account.getId(),
                orderId, AmountDirection.OUT.getType(), businessType.getCode(), amount, "冻结");
        accountDetailService.insert(accountDetail);
        return true;
    }
    /**
     * 根据用户ID和币种名称查询资金账户
     *
     * @param coinId 币种ID
     * @return
     */
    @Override
    public Account queryByUserIdAndCoinId(long userId, long coinId) {
//        EntityWrapper<Account> wrapper = new EntityWrapper<>();
//        wrapper.eq("user_id", userId)
//                .eq("coin_id", coinId)
//                .eq("status", BaseStatus.EFFECTIVE.getCode())
//                .last("LIMIT 1");
//        List<Account> accountList = baseMapper.selectList(wrapper);
//        if (CollectionUtils.isEmpty(accountList)) {
//            return null;
//        }
//        return accountList.get(0);
        Map<String,Object> param=new HashMap<>();
        param.put("userId",userId);
        param.put("coinId",coinId);
        param.put("status",BaseStatus.EFFECTIVE.getCode());
        Account account=baseMapper.queryByUserIdAndCoinId(param);

        return account;
    }

    @Override
    @Transactional
    public boolean addAmount(Long accountId, BigDecimal amount, BusinessType businessType, String remark, Long orderId) {
        Account account = selectById(accountId);
        if (null == account) {
            throw new AccountException("资金账户不存在");
        }
        account.setBalanceAmount(account.getBalanceAmount().add(amount));
        AccountDetail accountDetail = new AccountDetail(account.getUserId(), account.getCoinId(),
                account.getId(), account.getId(), orderId,
                AmountDirection.INCOME.getType(), businessType.getCode(), amount, remark);
        accountDetailService.insert(accountDetail);
        updateById(account);
        return true;
    }

    @Override
    public BigDecimal selectAmount(String id) {
        return baseMapper.selectAmount(id);
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
    public boolean unlockAmount(long userId,
                                long coinId,
                                BigDecimal amount,
                                BusinessType businessType,
                                long orderId) throws AccountException {
        Account account = this.queryByUserIdAndCoinId(userId, coinId);
        return unlockAmount0(account, amount, businessType, orderId);
    }

    public boolean unlockAmount(long accountId,
                                BigDecimal amount,
                                BusinessType businessType,
                                long orderId) {
        Account account = selectById(accountId);
        return unlockAmount0(account, amount, businessType, orderId);
    }



    private boolean unlockAmount0(Account account,
                                  BigDecimal amount,
                                  BusinessType businessType,
                                  long orderId) throws AccountException {
        if (account == null) {
            log.error("解冻资金-资金账户异常，userId:{}, coinId:{}", account.getUserId(), account.getCoinId());
            throw new AccountException("资金账户异常");
        }
        if (baseMapper.unlockAmount(account.getId(), amount) > 0) {
            // 保存流水
            AccountDetail accountDetail = new AccountDetail(account.getUserId(), account.getCoinId(), account.getId(), account.getId(),
                    orderId, AmountDirection.INCOME.getType(), businessType.getCode(), amount, "解冻");
            accountDetailService.insert(accountDetail);
            return true;
        }
        log.error("解冻资金失败，orderId:{}, userId:{}, coinId:{}, amount:{}, businessType:{}",
                orderId, account.getUserId(), account.getCoinId(), amount, businessType.getCode());
        throw new AccountException("解冻资金失败");
    }

    @Override
    public boolean unlockCashAmount(long userId,
                                    long coinId,
                                    BigDecimal amount,
                                    BusinessType businessType,
                                    long orderId) throws AccountException {
        Account account = this.queryByUserIdAndCoinId(userId, coinId);
        if (account == null) {
            log.error("解冻资金-资金账户异常，userId:{}, coinId:{}", userId, coinId);
            throw new AccountException("资金账户异常");
        }
        if (baseMapper.unlockCashAmount(account.getId(), amount) > 0) {
            // 保存流水
            AccountDetail accountDetail = new AccountDetail(userId, coinId, account.getId(), account.getId(),
                    orderId, AmountDirection.INCOME.getType(), businessType.getCode(), amount, "解冻");
            accountDetailService.insert(accountDetail);
            return true;
        }
        log.error("解冻资金失败，orderId:{}, userId:{}, coinId:{}, amount:{}, businessType:{}",
                orderId, userId, coinId, amount, businessType.getCode());
        throw new AccountException("解冻资金失败");
    }

    @Override
    public boolean subtractAmount(long userId,
                                  long coinId,
                                  BigDecimal fee,
                                  BigDecimal amount,
                                  BusinessType businessType,
                                  String remark,
                                  long orderId) throws AccountException {
        Account account = this.queryByUserIdAndCoinId(userId, coinId);
        return subtractAmount0(account, fee, amount, businessType, remark, orderId);
    }

    @Override
    public boolean subtractAmount(long accountId,
                                  BigDecimal fee,
                                  BigDecimal amount,
                                  BusinessType businessType,
                                  String remark,
                                  long orderId) throws AccountException {
        Account account = this.selectById(accountId);
        return subtractAmount0(account, fee, amount, businessType, remark, orderId);
    }

    private boolean subtractAmount0(Account account,
                                    BigDecimal fee,
                                    BigDecimal amount,
                                    BusinessType businessType,
                                    String remark,
                                    long orderId) throws AccountException {
        //   Account account = this.queryByUserIdAndCoinId(userId, coinId);
        if (account == null) {
            log.error("扣减资金-资金账户异常，userId:{}, coinId:{}", account.getUserId(), account.getCoinId());
            throw new com.blockeng.framework.exception.AccountException("资金账户异常");
        }
        if (baseMapper.subtractAmount(account.getId(), amount) > 0) {
            // 保存流水
            AccountDetail accountDetail = new AccountDetail(account.getUserId(), account.getCoinId(),
                    account.getId(),
                    account.getId(),
                    orderId,
                    AmountDirection.OUT.getType(),
                    businessType.getCode(),
                    amount,
                    fee,
                    remark);
            accountDetailService.insert(accountDetail);
            return true;
        }
        log.error("扣减资金失败，orderId:{}, userId:{}, coinId:{}, amount:{}, businessType:{}",
                orderId, account.getUserId(), account.getCoinId(), amount, businessType.getCode());
        throw new com.blockeng.framework.exception.AccountException("扣减资金失败");
    }

    /**
     * 资金划转
     *
     * @param fromUserId   转出用户ID
     * @param toUserId     转入用户ID
     * @param coinId       币种ID
     * @param amount       金额
     * @param businessType 业务类型
     * @param orderId      关联订单号
     * @param remark       备注
     * @return
     */
    @Override
    public boolean transferAmount(long fromUserId,
                                  long toUserId,
                                  long coinId,
                                  BigDecimal amount,
                                  BusinessType businessType,
                                  long orderId,
                                  String remark) throws AccountException {
        Account fromAccount = this.queryByUserIdAndCoinId(fromUserId, coinId);
        if (fromAccount == null) {
            log.error("资金划转-资金账户异常，userId:{}, coinId:{}", fromUserId, coinId);
            throw new AccountException("资金账户异常");
        }
        Account toAccount = this.queryByUserIdAndCoinId(toUserId, coinId);
        if (toAccount == null) {
            log.error("资金划转-资金账户异常，userId:{}, coinId:{}", toUserId, coinId);
            throw new AccountException("资金账户异常");
        }
        // 扣减资金
        if (baseMapper.subtractAmount(fromAccount.getId(), amount) > 0) {
            // 保存流水
            AccountDetail fromAccountDetail = new AccountDetail(fromUserId,
                    coinId,
                    fromAccount.getId(),
                    toAccount.getId(),
                    orderId,
                    AmountDirection.OUT.getType(),
                    businessType.getCode(),
                    amount,
                    remark);
            accountDetailService.insert(fromAccountDetail);
            // 增加资金
            if (baseMapper.addAmount(toAccount.getId(), amount) > 0) {
                // 保存流水
                AccountDetail toAccountDetail = new AccountDetail(toUserId,
                        coinId,
                        toAccount.getId(),
                        fromAccount.getId(),
                        orderId,
                        AmountDirection.INCOME.getType(),
                        businessType.getCode(),
                        amount,
                        remark);
                accountDetailService.insert(toAccountDetail);
                return true;
            }
        }
        log.error("资金划转，orderId:{}, fromUserId:{}, toUserId:{}, coinId:{}, amount:{}, businessType:{}, remark:{}",
                orderId, fromUserId, toUserId, coinId, amount, businessType.getCode(), remark);
        throw new AccountException("资金划转失败");
    }

    /**
     * 平台充值
     *
     * @param businessType 业务类型
     * @return
     * @throws AccountException
     */
    @Override
    public boolean rechargeAmount(AccountDTO accountDTO, BusinessType businessType) throws AccountException {
        User c2cAdmin = userService.queryAdminUser(AdminUserType.C2C_ADMIN);
        if (c2cAdmin == null) {
            log.error("尚未配置C2C管理员用户");
            throw new AccountException("尚未配置C2C管理员用户");
        }
        // if (c2cAdmin.getId().equals(accountDTO.getUserId())) {
        //     log.error("充值用户是c2c管理员");
        //     throw new AccountException("充值用户不能是c2c管理员");
        // }
        // fromUserId 转出用户ID
        Account fromAccount = this.queryByUserIdAndCoinId(c2cAdmin.getId(), accountDTO.getCoinId());
        if (fromAccount == null) {
            log.error("资金划转-资金账户异常，userId:{}, coinId:{}", c2cAdmin.getId(), accountDTO.getCoinId());
            throw new AccountException("资金账户异常");
        }
        // 转入用户ID
        Account toAccount = this.queryByUserIdAndCoinId(accountDTO.getUserId(), accountDTO.getCoinId());
        if (toAccount == null) {
            log.error("资金划转-资金账户异常，userId:{}, coinId:{}", accountDTO.getUserId(), accountDTO.getCoinId());
            throw new AccountException("资金账户异常");
        }
        if (!fromAccount.getUserId().equals(c2cAdmin.getId())) {
            //账面资金大于等于充值金额
            if (fromAccount.getBalanceAmount().compareTo(accountDTO.getAmount()) == -1) {
                throw new AccountException("C2C管理员账号资金不够");
            }
            // 扣减资金
            if (baseMapper.subtractAmount(fromAccount.getId(), accountDTO.getAmount()) > 0) {
                // 保存流水
                AccountDetail fromAccountDetail = new AccountDetail(c2cAdmin.getId(),
                        accountDTO.getCoinId(),
                        fromAccount.getId(),
                        toAccount.getId(),
                        0L,
                        AmountDirection.OUT.getType(),
                        businessType.getCode(),
                        accountDTO.getAmount(),
                        "后台充值");
                accountDetailService.insert(fromAccountDetail);
                // 增加资金
                if (baseMapper.addAmount(toAccount.getId(), accountDTO.getAmount()) > 0) {
                    // 保存流水
                    AccountDetail toAccountDetail = new AccountDetail(toAccount.getUserId(),
                            accountDTO.getCoinId(),
                            toAccount.getId(),
                            fromAccount.getId(),
                            0L,
                            AmountDirection.INCOME.getType(),
                            businessType.getCode(),
                            accountDTO.getAmount(),
                            "后台充值");
                    accountDetailService.insert(toAccountDetail);
                    return true;
                }
            }
        } else {
            // 增加资金
            if (baseMapper.addAmount(toAccount.getId(), accountDTO.getAmount()) > 0) {
                // 保存流水
                AccountDetail toAccountDetail = new AccountDetail(toAccount.getUserId(),
                        accountDTO.getCoinId(),
                        toAccount.getId(),
                        fromAccount.getId(),
                        0L,
                        AmountDirection.INCOME.getType(),
                        businessType.getCode(),
                        accountDTO.getAmount(),
                        "后台充值");
                accountDetailService.insert(toAccountDetail);
                return true;
            }
        }
        throw new AccountException("充值失败");
    }

    @Override
    public int addAmount(Long id, BigDecimal releaseAmount) {
        return this.baseMapper.addAmount(id,releaseAmount);
    }

    @Override
    public Integer selectBalanceCountByCoinId(String id) {
        return this.baseMapper.selectBalanceCountByCoinId( id);
    }


    @Override
    public List<Account> selectListPageFromAccount(int current, int size, Wrapper<Account> ew, Wrapper<User> ewOther) {
        return this.baseMapper.selectListPageFromAccount(current, size, ew, ewOther);
    }

    @Override
    public Integer selectListPageCount() {
        return this.baseMapper.selectListPageCount();
    }

    @Override
    public Integer selectListPageCountFromAccount(Wrapper<Account> ew, Wrapper<User> userEw) {
        return this.baseMapper.selectListPageCountFromAccount(ew, userEw);
    }

    @Override
    public Integer selectListPageCountFromUser(Wrapper<User> ew, Wrapper<Account> accountEw) {
        return this.baseMapper.selectListPageCountFromUser(ew, accountEw);
    }

    @Override
    public List<Account> selectListPageEmpty(int current, int size) {
        return this.baseMapper.selectListPageEmpty(current, size);
    }

    @Override
    public List<Account> selectListPageFromUser(int current, int size, Wrapper<User> ew, Wrapper<Account> accountEw) {
        return this.baseMapper.selectListPageFromUser(current, size, ew, accountEw);
    }
}
