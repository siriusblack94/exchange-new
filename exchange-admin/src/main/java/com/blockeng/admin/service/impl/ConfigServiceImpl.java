package com.blockeng.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.blockeng.admin.entity.Config;
import com.blockeng.admin.mapper.ConfigMapper;
import com.blockeng.admin.service.ConfigService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 平台配置信息 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {

    /**
     * 查询系统配置
     *
     * @param type 类型
     * @param code 代码
     * @return
     */
    @Override
    public Config queryBuyCodeAndType(String type, String code) {
        EntityWrapper<Config> wrapper = new EntityWrapper<>();
        wrapper.eq("type", type).eq("code", code);
        List<Config> configs = baseMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(configs)) {
            return null;
        }
        return configs.get(0);
    }
}
