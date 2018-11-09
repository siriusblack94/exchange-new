package com.blockeng.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.trade.entity.Config;
import com.blockeng.trade.mapper.ConfigMapper;
import com.blockeng.trade.service.ConfigService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 平台配置信息 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {

    /**
     * 根据code查询配置信息
     *
     * @param type 代码类型
     * @param code 配置代码
     * @return
     */
    @Override
    public Config queryByTypeAndCode(String type, String code) {
        QueryWrapper<Config> wrapper = new QueryWrapper<>();
        wrapper.eq("type", type)
                .eq("code", code);
        return super.selectOne(wrapper);
    }
}
