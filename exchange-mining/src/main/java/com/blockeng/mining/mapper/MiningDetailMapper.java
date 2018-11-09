package com.blockeng.mining.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockeng.mining.dto.FeeDTO;
import com.blockeng.mining.entity.MiningDetail;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: 挖矿统计
 * @Author: Chen Long
 * @Date: Created in 2018/6/30 上午12:11
 * @Modified by: Chen Long
 */
public interface MiningDetailMapper extends BaseMapper<MiningDetail> {

    void updateMining(@Param("totalFee") BigDecimal totalFee,
                      @Param("areaName") String area_name,
                      @Param("miningTime") String miningTime,
                      @Param("userId") Long userId);


    List<FeeDTO> dayTotalFee(@Param("time_mining") String timeMining);
}
