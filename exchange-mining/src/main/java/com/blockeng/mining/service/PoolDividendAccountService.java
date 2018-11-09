package com.blockeng.mining.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.mining.dto.PoolDividendTotalAccountDTO;
import com.blockeng.mining.entity.PoolDividendAccount;

import java.math.BigDecimal;

/**
 * <p>
 * 矿池 数据查询
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */

public interface PoolDividendAccountService extends IService<PoolDividendAccount> {


    Object poolAccountList(Page<PoolDividendAccount> page, Long id);

    PoolDividendTotalAccountDTO selectPriTotalUnAcount(Long id);

    Object dividendAccountTotal(Long id);


}
