package com.blockeng.mining.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockeng.mining.entity.Account;
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
    int addAmount(@Param("accountId") long accountId, @Param("amount") BigDecimal amount);
}
