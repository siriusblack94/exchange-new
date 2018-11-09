package com.blockeng.mining.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockeng.mining.entity.Mine;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * @Description: 挖矿统计
 * @Author: Chen Long
 * @Date: Created in 2018/6/30 上午12:11
 * @Modified by: Chen Long
 */
public interface MineMapper extends BaseMapper<Mine> {

    BigDecimal totalMine();

    BigDecimal dayTotalMine(@Param("time_mining") String timeMining);

    BigDecimal mineTotal(@Param("ew") Wrapper<Mine> ew);

}
