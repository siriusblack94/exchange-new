package com.blockeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.entity.Config;
import com.blockeng.mapper.ConfigMapper;
import com.blockeng.service.ConfigService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<Config> configs = baseMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(configs)) {
            return null;
        }
        return configs.get(0);
    }

    /**
     * 根据类型查询配置
     *
     * @param type 代码类型
     * @return
     */
    @Override
    public Map<String, String> queryByType(String type) {
        QueryWrapper<Config> wrapper = new QueryWrapper<>();
        wrapper.eq("type", type);
        Map<String, String> configMap = new HashMap<>();
        baseMapper.selectList(wrapper).forEach(config -> {
            configMap.put(config.getCode(), config.getValue());
        });
        return configMap;
    }
}
