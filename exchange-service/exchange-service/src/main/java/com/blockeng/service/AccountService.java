package com.blockeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.dto.SymbolAssetDTO;
import com.blockeng.entity.Account;
import com.blockeng.framework.dto.ApplyWithdrawDTO;
import com.blockeng.framework.enums.BusinessType;
import com.blockeng.framework.exception.AccountException;
import com.blockeng.framework.http.Response;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 用户财产记录 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface AccountService extends IService<Account> {

    /**
     * 根据用户ID和币种名称查询资金账户
     *
     * @param userId   用户ID
     * @param coinName 币种名称
     * @return
     */
    Account selectByUserAndCoinName(long userId, String coinName);

    /**
     * 根据用户ID和币种名称查询资金账户
     *
     * @param userId 用户ID
     * @param coinId 币种ID
     * @return
     */
    Account queryByUserIdAndCoinId(long userId, long coinId);


    /**
     * 根据用户ID和币种名称查询资金账户
     *
     * @param userId        用户ID
     * @param coinId        币种ID
     * @param containEnable 是否包含冻结
     * @return
     */
    Account queryByUserIdAndCoinId(long userId, long coinId, boolean containEnable);

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
    boolean lockAmount(long userId,
                       long coinId,
                       BigDecimal amount,
                       BusinessType businessType,
                       long orderId) throws AccountException;

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

    /**
     * 增加资金
     *
     * @param userId       用户ID
     * @param coinId       币种ID
     * @param amount       冻结金额
     * @param businessType 业务类型
     * @param remark       备注
     * @param orderId      关联订单号
     * @return
     */
    boolean addAmount(long userId,
                      long coinId,
                      BigDecimal amount,
                      BusinessType businessType,
                      String remark,
                      long orderId) throws AccountException;
    /**
     * 增加奖励资金
     *
     * @param userId       用户ID
     * @param coinId       币种ID
     * @param amount       奖励金额
     * @param businessType 奖励类型
     * @param remark       备注
     * @return
     */
    boolean addAmountReward(long userId,
                            long coinId,
                            BigDecimal amount,
                            BusinessType businessType,
                            String remark)throws AccountException;

    /**
     * 扣减资金-->这个接口 其实是提现成功的接口 提币审核通过后走到的 只改变了提现和冻结
     *
     * @param userId       用户ID
     * @param coinId       币种ID
     * @param amount       冻结金额
     * @param businessType 业务类型
     * @param remark       备注
     * @param orderId      关联订单号
     * @return
     */
    boolean subtractAmount(long userId,
                           long coinId,
                           BigDecimal amount,
                           BusinessType businessType,
                           String remark,
                           long orderId) throws AccountException;


    /**
     * 扣减资金-- 直接扣减用户的balanceAmount
     * @param userId       用户ID
     * @param coinId       币种ID
     * @param amount       冻结金额
     * @param businessType 业务类型
     * @param remark       备注
     * @param orderId      关联订单号
     * @return
     */
    boolean subtractBalanceAmount(long userId,
                           long coinId,
                           BigDecimal amount,
                           BusinessType businessType,
                           String remark,
                           long orderId) throws AccountException;

    /**
     * 统计账户资产
     *
     * @param userId
     * @return
     */
    Map<String, Object> countAssets(Long userId);

    /**
     * 币币交易用户交易对账户资产
     *
     * @param symbol 交易对标识符
     * @param userId 用户ID
     */
    SymbolAssetDTO queryAccount(String symbol, long userId);

    /**
     * 获取钱包充值地址
     *
     * @param coinId 币种ID
     * @param userId 用户ID
     * @return
     */
    @Deprecated
    String queryRechargeAddress(Long coinId, Long userId);

    /**
     * 申请提币
     *
     * @param applyWithdraw 申请提币参数
     * @param userId        用户ID
     */
    Response applyWithdraw(ApplyWithdrawDTO applyWithdraw, Long userId);

    /**
     * 注册同步币种账户和外汇账户
     */
    boolean syncAccount(long userId);

    /**
     * 解冻邀请奖励
     *
     * @param userId
     * @return
     */
    boolean unfreezeInviteRewards(long userId);



}
