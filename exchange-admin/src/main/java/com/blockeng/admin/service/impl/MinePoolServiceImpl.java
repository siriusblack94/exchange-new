package com.blockeng.admin.service.impl;

import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.admin.dto.MinePoolDTO;
import com.blockeng.admin.dto.PoolDividendAccountDTO;
import com.blockeng.admin.entity.MinePool;
import com.blockeng.admin.mapper.MinePoolMapper;
import com.blockeng.admin.service.MinePoolService;
import org.springframework.stereotype.Service;

/**
 * @Description: 矿池成员 服务实现类
 * @Author: Chen Long
 * @Date: Created in 2018/6/29 下午4:00
 * @Modified by: Chen Long
 */
@Service
public class MinePoolServiceImpl extends ServiceImpl<MinePoolMapper, MinePool> implements MinePoolService {

    @Override
    public Page<MinePoolDTO> getPoolListPage(Page<MinePoolDTO> page, Wrapper<MinePoolDTO> wrapper) {
        wrapper = (Wrapper<MinePoolDTO>) SqlHelper.fillWrapper(page, wrapper);
        page.setRecords(baseMapper.selectPoolListPage(page, wrapper));
        return page;
    }
}
