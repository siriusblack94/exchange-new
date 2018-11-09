package com.blockeng.admin.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.dto.PoolDividendRecordDTO;
import com.blockeng.admin.entity.PoolDividendRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface PoolDividendRecordMapper extends BaseMapper<PoolDividendRecord> {


    List<PoolDividendRecordDTO> selectPoolDividendRecordList(Page<PoolDividendRecordDTO> page, @Param("ew") Wrapper<PoolDividendRecordDTO> wrapper);
}
