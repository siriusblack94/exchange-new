package com.blockeng.admin.service.impl;



import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.admin.dto.DividendAccountDTO;
import com.blockeng.admin.dto.DividendRecordDetailDTO;
import com.blockeng.admin.dto.DividendReleaseRecordDTO;
import com.blockeng.admin.entity.DividendRecord;
import com.blockeng.admin.entity.DividendRecordDetail;
import com.blockeng.admin.entity.DividendReleaseRecord;
import com.blockeng.admin.mapper.DividendRecordDetailMapper;
import com.blockeng.admin.mapper.DividendRecordMapper;
import com.blockeng.admin.mapper.DividendReleaseRecordMapper;
import com.blockeng.admin.service.DividendRecordService;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@Transactional
public class DividendRecordServiceImpl extends ServiceImpl<DividendRecordMapper, DividendRecord> implements DividendRecordService {
    @Autowired
    DividendRecordDetailMapper dividendRecordDetailMapper;

    @Autowired
    DividendReleaseRecordMapper dividendReleaseRecordMapper;
    @Override
    public Page<DividendRecordDetailDTO> selectDetailListPage(Page<DividendRecordDetailDTO> page, Wrapper<DividendRecordDetailDTO> wrapper) {
        wrapper = (Wrapper<DividendRecordDetailDTO>) SqlHelper.fillWrapper(page, wrapper);
        page.setRecords(dividendRecordDetailMapper.selectDetailListPage(page, wrapper));
        return page;
    }

    public Page<DividendReleaseRecordDTO> selectReleaseListPage(Page<DividendReleaseRecordDTO> page, Wrapper<DividendReleaseRecordDTO> wrapper) {
        wrapper = (Wrapper<DividendReleaseRecordDTO>) SqlHelper.fillWrapper(page, wrapper);
        page.setRecords(dividendReleaseRecordMapper.selectReleaseDetailListPage(page, wrapper));
        return page;
    }






}
