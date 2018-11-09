package com.blockeng.extend.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockeng.extend.entity.Account;
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
    int lockAmount(@Param("accountId") long accountId, @Param("amount") BigDecimal amount);
    int subtractAmount(@Param("accountId") long accountId,
                       @Param("amount") BigDecimal amount);

    int addAmount(@Param("accountId") long accountId,
                       @Param("amount") BigDecimal amount);

    Account selectByUserAndCoinName(@Param("userId") String userId,
                                    @Param("coinName") String coinName);
    Account selectByUserAndCoinId(@Param("userId") long userId,
                                    @Param("coinId") long coinId);


    int unLockAmount(long accountId, BigDecimal amount);
}
