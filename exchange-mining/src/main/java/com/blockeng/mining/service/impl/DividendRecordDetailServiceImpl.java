package com.blockeng.mining.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.mining.entity.DividendRecordDetail;
import com.blockeng.mining.dto.DividendRecordDetailDTO;
import com.blockeng.mining.mapper.DividendRecordDetailMapper;
import com.blockeng.mining.service.DividendRecordDetailService;
import org.springframework.stereotype.Service;

/**
 * @Auther: sirius
 * @Date: 2018/10/12 11:23
 * @Description:
 */
@Service
public class DividendRecordDetailServiceImpl  extends ServiceImpl<DividendRecordDetailMapper, DividendRecordDetail> implements DividendRecordDetailService {
    @Override
    public Page<DividendRecordDetailDTO> selectListPage(Page<DividendRecordDetailDTO> page, Wrapper<DividendRecordDetailDTO> wrapper) {
        wrapper = (Wrapper<DividendRecordDetailDTO>) SqlHelper.fillWrapper(page, wrapper);
        page.setRecords(baseMapper.selectListPage(page, wrapper));
        return page;
    }

}
