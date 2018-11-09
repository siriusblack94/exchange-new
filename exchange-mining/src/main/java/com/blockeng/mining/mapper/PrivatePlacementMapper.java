package com.blockeng.mining.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockeng.mining.entity.PrivatePlacement;

import java.math.BigDecimal;

/**
 * @Auther: sirius
 * @Date: 2018/8/15 13:20
 * @Description:
 */
public interface PrivatePlacementMapper extends BaseMapper<PrivatePlacement> {
    BigDecimal totalAmount();

    BigDecimal totalReleaseAmount();
}
