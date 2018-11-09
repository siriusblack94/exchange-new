package com.blockeng.admin.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.blockeng.admin.dto.AccountDTO;
import com.blockeng.admin.entity.Account;
import com.blockeng.admin.entity.User;
import com.blockeng.admin.entity.WalletInfo;
import com.blockeng.framework.enums.BusinessType;
import com.blockeng.framework.exception.AccountException;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 用户财产记录 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
public interface AccountService extends IService<Account> {

    /**
     * 分页查询
     *
     * @param page
     * @param wrapper
     * @return
     */
    Page<Account> selectListPage(Page<Account> page, Wrapper<Account> wrapper);


    /**
     * 分页查询
     *
     * @param ew account
     * @return
     */
    List<Account> selectListPageEmpty(int current, int size);


    /**
     * 分页查询
     *
     * @param ew        account
     * @param accountEw User
     * @return
     */
    List<Account> selectListPageFromUser(int current, int size, Wrapper<User> ew, Wrapper<Account> accountEw);


    /**
     * 空查询
     *
     * @param accountEw account
     * @param userEw    User
     * @return 空查询
     */
    List<Account> selectListPageFromAccount(int current, int size, Wrapper<Account> accountEw, Wrapper<User> userEw);

    /**
     * count
     *
     * @return 查询总是,
     */
    Integer selectListPageCount();

    /**
     * count
     *
     * @param accountEw account
     * @return 更具ew查询总数
     */
    Integer selectListPageCountFromAccount(Wrapper<Account> accountEw, Wrapper<User> userEw);

    /**
     * count
     *
     * @param userEw account
     * @return 更具user查询总数
     */
    Integer selectListPageCountFromUser(Wrapper<User> userEw, Wrapper<Account> accountEw);


    /**
     * 查询资金账户
     *
     * @param userId 用户ID
     * @param coinId 币种ID
     * @return
     */
    Account queryByUserIdAndCoinId(long userId, long coinId);

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
    boolean unlockAmount(long userId,
                         long coinId,
                         BigDecimal amount,
                         BusinessType businessType,
                         long orderId) throws AccountException;

    boolean unlockAmount(long accountId, BigDecimal amount,
                         BusinessType businessType,
                         long orderId) throws AccountException;


    boolean unlockCashAmount(long userId,
                             long coinId,
                             BigDecimal amount,
                             BusinessType businessType,
                             long orderId);

    /**
     * 扣减资金
     *
     * @param userId       用户ID
     * @param coinId       币种ID
     * @param fee
     * @param amount       冻结金额
     * @param businessType 业务类型
     * @param remark       备注
     * @param orderId      关联订单号
     * @return
     */
    boolean subtractAmount(long userId,
                           long coinId,
                           BigDecimal fee,
                           BigDecimal amount,
                           BusinessType businessType,
                           String remark,
                           long orderId) throws AccountException;

    boolean subtractAmount(long accountId,
                           BigDecimal fee,
                           BigDecimal amount,
                           BusinessType businessType,
                           String remark,
                           long orderId) throws AccountException;

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
    boolean transferAmount(long fromUserId,
                           long toUserId,
                           long coinId,
                           BigDecimal amount,
                           BusinessType businessType,
                           long orderId,
                           String remark) throws AccountException;

    /**
     * 后台充值
     *
     * @param accountDTO
     * @param businessType 业务类型
     * @return
     * @throws AccountException
     */
    boolean rechargeAmount(AccountDTO accountDTO,
                           BusinessType businessType) throws AccountException;

    int addAmount(Long id, BigDecimal releaseAmount);

    Integer selectBalanceCountByCoinId(String id);

    boolean lockAmount(Long accountId, BigDecimal amount, Long id, BusinessType buckleLock);

    boolean addAmount(Long accountId, BigDecimal amount, BusinessType buckle, String desc, Long id);


    BigDecimal selectAmount(String id);
}
