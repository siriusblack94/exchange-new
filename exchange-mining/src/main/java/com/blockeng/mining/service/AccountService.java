package com.blockeng.mining.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.mining.entity.Account;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 用户财产记录 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface AccountService extends IService<Account> {

    Account getMineCoinInfo(Long userId);
    BigDecimal selectTotal(Long coinId);

    List<Account> selectListByFlag();
}
