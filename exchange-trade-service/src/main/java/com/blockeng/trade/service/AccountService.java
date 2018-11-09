package com.blockeng.trade.service;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.framework.enums.BusinessType;
import com.blockeng.framework.exception.AccountException;
import com.blockeng.trade.entity.Account;

import java.math.BigDecimal;

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
     * @param userId 用户ID
     * @param coinId 币种ID
     * @return
     */
    @Cached(name = "ACCOUNT", expire = 1200, cacheType = CacheType.LOCAL)
    Account queryByUserIdAndCoinId(long userId, long coinId);

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
    boolean lockAmount(Long userId,
                       Long coinId,
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
    boolean unlockAmount(Long userId,
                         Long coinId,
                         BigDecimal amount,
                         BusinessType businessType,
                         Long orderId) throws AccountException;

    /**
     * 扣减资金
     *
     * @param accountId 资金账户ID
     * @return
     */
    int subtractAmount(Long accountId, BigDecimal amount);
}
