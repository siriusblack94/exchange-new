package com.blockeng.mining.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockeng.mining.entity.PoolDividendRecord;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface PoolDividendRecordMapper extends BaseMapper<PoolDividendRecord> {

    BigDecimal selectTotalUnAcount(@Param("ew") QueryWrapper<PoolDividendRecord> qw);

}
