package com.blockeng.boss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockeng.boss.entity.Account;
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
     * 扣减账户资金
     *
     * @param accountId 资金账户ID
     * @param amount    扣减资金额度
     * @return
     */
    int subtractAmount(@Param("accountId") Long accountId, @Param("amount") BigDecimal amount);
}
