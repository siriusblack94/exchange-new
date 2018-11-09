package com.blockeng.admin.service.impl;




import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import com.blockeng.admin.dto.PoolDividendRecordDTO;
import com.blockeng.admin.entity.PoolDividendRecord;
import com.blockeng.admin.mapper.PoolDividendRecordMapper;
import com.blockeng.admin.service.PoolDividendRecordService;


import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




@Service
@Slf4j
@Transactional
public class PoolDividendRecordServiceImpl extends ServiceImpl<PoolDividendRecordMapper, PoolDividendRecord> implements PoolDividendRecordService {


    @Override
    public Page<PoolDividendRecordDTO> getPoolDividendRecordList(Page<PoolDividendRecordDTO> page, Wrapper<PoolDividendRecordDTO> wrapper) {
        wrapper = (Wrapper<PoolDividendRecordDTO>) SqlHelper.fillWrapper(page, wrapper);
        page.setRecords(baseMapper.selectPoolDividendRecordList(page, wrapper));
        return page;

    }
}
