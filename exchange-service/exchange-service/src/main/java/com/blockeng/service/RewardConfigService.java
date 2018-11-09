package com.blockeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.entity.RewardConfig;

import java.util.List;

/**
 * <p>
 * 注册，推荐奖励规则
 * </p>
 *
 * @author shaodw
 * @since 2018-09-18
 */
public interface RewardConfigService extends IService<RewardConfig> {
    List<RewardConfig> queryByType(String type);
}
