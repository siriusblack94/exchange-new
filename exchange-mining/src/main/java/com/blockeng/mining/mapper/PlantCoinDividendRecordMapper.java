package com.blockeng.mining.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockeng.mining.dto.PlantCoinDividendTotalDTO;
import com.blockeng.mining.entity.PlantCoinDividendRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlantCoinDividendRecordMapper extends BaseMapper<PlantCoinDividendRecord> {


    List<PlantCoinDividendTotalDTO> selectTotalAmount(@Param("ew") Wrapper<PlantCoinDividendRecord> ew);

    List<String> selectDate(@Param("userId") Long id);
}
