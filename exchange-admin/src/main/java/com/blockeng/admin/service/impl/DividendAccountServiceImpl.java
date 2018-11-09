package com.blockeng.admin.service.impl;




import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.admin.dto.DividendAccountDTO;

import com.blockeng.admin.entity.DividendAccount;
import com.blockeng.admin.mapper.DividendAccountMapper;
import com.blockeng.admin.service.DividendAccountService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Slf4j
@Transactional
public class DividendAccountServiceImpl extends ServiceImpl<DividendAccountMapper, DividendAccount> implements DividendAccountService {

    @Override
    public Page<DividendAccountDTO> selectListPage(Page<DividendAccountDTO> page, Wrapper<DividendAccountDTO> wrapper) {
        wrapper = (Wrapper<DividendAccountDTO>) SqlHelper.fillWrapper(page, wrapper);
        page.setRecords(baseMapper.selectListPage(page, wrapper));
        return page;
    }





}
