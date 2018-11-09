package com.blockeng.admin.service;



import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.blockeng.admin.dto.PoolDividendAccountDTO;
import com.blockeng.admin.dto.PoolDividendRecordDTO;
import com.blockeng.admin.entity.PoolDividendRecord;

import java.util.List;


/**
 * <p>
 * 矿池 分红数据
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */

public interface PoolDividendRecordService extends IService<PoolDividendRecord> {


    Page<PoolDividendRecordDTO> getPoolDividendRecordList(Page<PoolDividendRecordDTO> page, Wrapper<PoolDividendRecordDTO> wrapper);
}
