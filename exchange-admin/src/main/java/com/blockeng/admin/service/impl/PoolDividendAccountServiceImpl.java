package com.blockeng.admin.service.impl;



import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.admin.dto.PoolDividendAccountDTO;
import com.blockeng.admin.entity.PoolDividendAccount;
import com.blockeng.admin.mapper.PoolDividendAccountMapper;
import com.blockeng.admin.service.PoolDividendAccountService;


import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@Transactional
public class PoolDividendAccountServiceImpl extends ServiceImpl<PoolDividendAccountMapper, PoolDividendAccount> implements PoolDividendAccountService {


    @Override
    public Page<PoolDividendAccountDTO> getPoolDividendAccountList(Page<PoolDividendAccountDTO> page, Wrapper<PoolDividendAccountDTO> wrapper) {
        wrapper = (Wrapper<PoolDividendAccountDTO>) SqlHelper.fillWrapper(page, wrapper);
        page.setRecords(baseMapper.selectPoolDividendAccountListPage(page, wrapper));
        return page;
    }

    @Override
    public Page<PoolDividendAccountDTO> getPoolDividendAccountDetailList(Page<PoolDividendAccountDTO> page, Wrapper<PoolDividendAccountDTO> wrapper) {
        wrapper = (Wrapper<PoolDividendAccountDTO>) SqlHelper.fillWrapper(page, wrapper);
        page.setRecords(baseMapper.selectPoolDividendAccountDetailList(page, wrapper));
        return page;
    }
}
