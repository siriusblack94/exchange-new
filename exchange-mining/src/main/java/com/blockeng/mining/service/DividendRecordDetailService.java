package com.blockeng.mining.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.mining.entity.DividendRecordDetail;
import com.blockeng.mining.dto.DividendRecordDetailDTO;

/**
 * @Auther: sirius
 * @Date: 2018/10/12 11:22
 * @Description:
 */
public interface DividendRecordDetailService extends IService<DividendRecordDetail> {
     Page<DividendRecordDetailDTO> selectListPage(Page<DividendRecordDetailDTO> page, Wrapper<DividendRecordDetailDTO> wrapper);
}
