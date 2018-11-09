package com.blockeng.admin.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.dto.DividendAccountDTO;
import com.blockeng.admin.entity.DividendRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface DividendRecordMapper extends BaseMapper<DividendRecord> {

    List<DividendRecord> selectListPage(Page<DividendRecord> page, @Param("ew") Wrapper<DividendRecord> wrapper);
}
