package com.blockeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.entity.RewardConfig;
import com.blockeng.mapper.RewardConfigMapper;
import com.blockeng.service.RewardConfigService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 注册，推荐奖励规则
 * </p>
 *
 * @author shaodw
 * @since 2018-09-18
 */
@Service
public class RewardConfigServiceImpl extends ServiceImpl<RewardConfigMapper,RewardConfig> implements RewardConfigService {

    @Override
    public List<RewardConfig> queryByType(String type) {

        QueryWrapper<RewardConfig> qw = new QueryWrapper<>();
        qw.eq("type",type);
        return baseMapper.selectList(qw);
    }
}
