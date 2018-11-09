package com.blockeng.mining.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.mining.entity.PrivatePlacement;

import java.math.BigDecimal;


/**
 * @Auther: sirius
 * @Date: 2018/8/15 13:15
 * @Description:私募服务类
 */
public interface PrivatePlacementService extends IService<PrivatePlacement> {
    BigDecimal totalReleaseAmount();
}
