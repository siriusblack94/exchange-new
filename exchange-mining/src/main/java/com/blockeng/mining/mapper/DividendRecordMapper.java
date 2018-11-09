package com.blockeng.mining.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockeng.mining.dto.DividendDTO;
import com.blockeng.mining.entity.DividendRecord;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface DividendRecordMapper extends BaseMapper<DividendRecord> {

    BigDecimal selectTotalThisWeek(@Param("ew") Wrapper<DividendRecord> ew);

    BigDecimal selectAllDivide();

    List<DividendDTO> selectTotalGroupByUser();
}
