package com.blockeng.mining.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.mining.entity.PrivatePlacement;
import com.blockeng.mining.mapper.PrivatePlacementMapper;
import com.blockeng.mining.service.PrivatePlacementService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @Auther: EDZ
 * @Date: 2018/8/15 13:18
 * @Description:
 */
@Service
public class PrivatePlacementServiceImpl extends ServiceImpl<PrivatePlacementMapper, PrivatePlacement> implements PrivatePlacementService {
    @Override
    public BigDecimal totalReleaseAmount() {
        return baseMapper.totalReleaseAmount();
    }
}
