package com.blockeng.boss.service.impl;

import com.blockeng.boss.dto.Symbol;
import com.blockeng.boss.mapper.BossMapper;
import com.blockeng.boss.service.SymbolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/7/10 上午1:50
 * @Modified by: Chen Long
 */
@Service
public class SymbolServiceImpl implements SymbolService {

    @Autowired
    private BossMapper bossMapper;


    @Override
    public Symbol queryById(Long marketId) {
        return bossMapper.querySymbol(marketId);
    }
}
