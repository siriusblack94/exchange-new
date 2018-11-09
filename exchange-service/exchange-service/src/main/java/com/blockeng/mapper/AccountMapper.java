package com.blockeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockeng.entity.Account;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 * 用户财产记录 Mapper 接口
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface AccountMapper extends BaseMapper<Account> {

    Account selectByUserAndCoinName(@Param("userId") long userId,
                                    @Param("coinName") String coinName);

    /**
     * 冻结账户资金
     *
     * @param accountId 资金账户ID
     * @param amount    冻结金额
     * @return
     */
    int lockAmount(@Param("accountId") long accountId, @Param("amount") BigDecimal amount);

    /**
     * 解冻账户资金
     *
     * @param accountId 资金账户ID
     * @param amount    解冻金额
     * @return
     */
    int unlockAmount(@Param("accountId") long accountId, @Param("amount") BigDecimal amount);

    /**
     * 增加账户资金
     *
     * @param accountId 资金账户ID
     * @param amount    增加金额
     * @return
     */
    int addAmount(@Param("accountId") long accountId, @Param("amount") BigDecimal amount);

    /**
     * 扣减账户资金-->提现资产
     *
     * @param accountId 资金账户ID
     * @param amount    扣减金额
     * @return
     */
    int withdrawAmount(@Param("accountId") long accountId,
                       @Param("amount") BigDecimal amount);

    /**
     * 保存用户充值地址
     *
     * @param id      资金账户主键
     * @param address 钱包地址
     */
    void setRechargeAddress(@Param("id") Long id, @Param("address") String address);


    /**
     * 直接扣减用户余额
     * @param accountId 资金账户ID
     * @param amount    扣减金额
     * @return
     * */
    int subtractBalanceAmount(@Param("accountId") long accountId, @Param("amount") BigDecimal amount);
}
