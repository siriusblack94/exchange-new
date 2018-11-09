package com.blockeng.boss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.boss.entity.Market;
import com.blockeng.boss.mapper.MarketMapper;
import com.blockeng.boss.service.MarketService;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/7/19 下午3:32
 * @Modified by: Chen Long
 */
@Service
public class MarketServiceImpl extends ServiceImpl<MarketMapper, Market> implements MarketService {

    /**
     * 查询交易对(缓存2分钟)
     *
     * @param symbol 交易对标识符
     * @return
     */
    @Override
    public Market queryBySymbol(String symbol) {
        QueryWrapper<Market> wrapper = new QueryWrapper<>();
        wrapper.eq("symbol", symbol);
        return super.selectOne(wrapper);
    }
}
