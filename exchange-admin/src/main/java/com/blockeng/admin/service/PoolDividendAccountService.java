package com.blockeng.admin.service;



import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import com.blockeng.admin.dto.PoolDividendAccountDTO;
import com.blockeng.admin.entity.PoolDividendAccount;

import java.util.List;

/**
 * <p>
 * 矿池 数据查询
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */

public interface PoolDividendAccountService extends IService<PoolDividendAccount> {


    Page<PoolDividendAccountDTO> getPoolDividendAccountList(Page<PoolDividendAccountDTO> page,Wrapper<PoolDividendAccountDTO> wrapper);

    Page<PoolDividendAccountDTO> getPoolDividendAccountDetailList(Page<PoolDividendAccountDTO> page,Wrapper<PoolDividendAccountDTO> wrapper);
}
