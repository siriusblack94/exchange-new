package com.blockeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.entity.Coin;
import com.blockeng.mapper.CoinMapper;
import com.blockeng.service.CoinService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 币种配置信息 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
public class CoinServiceImpl extends ServiceImpl<CoinMapper, Coin> implements CoinService {

    /**
     * 根据主键查询币种信息
     *
     * @param coinId 币种ID
     * @return
     */
    @Override
    public Coin queryById(long coinId) {
        return baseMapper.selectById(coinId);
    }

    /**
     * 根据类型查询币种信息
     *
     * @param type 币种类型
     * @return
     */
    @Override
    public Coin queryByType(String type) {
        QueryWrapper<Coin> wrapper = new QueryWrapper<>();
        wrapper.eq("type", type);
        return super.selectOne(wrapper);
    }

    /**
     * 根据币种名称查询
     *
     * @param coinName 币种名称
     * @return
     */
    @Override
    public Coin queryByName(String coinName) {
        QueryWrapper<Coin> wrapper = new QueryWrapper<>();
        wrapper.eq("name", coinName);
        return super.selectOne(wrapper);
    }
}
