package com.blockeng.mining.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.mining.entity.Account;
import org.apache.ibatis.annotations.Param;

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
    int addAmount(long accountId,  BigDecimal amount);
}
