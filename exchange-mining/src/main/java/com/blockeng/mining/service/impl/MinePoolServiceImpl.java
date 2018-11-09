package com.blockeng.mining.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.mining.entity.MinePool;
import com.blockeng.mining.mapper.MinePoolMapper;
import com.blockeng.mining.service.MinePoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>
 * 挖矿统计
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */

@Service
@Slf4j
@Transactional
public class MinePoolServiceImpl extends ServiceImpl<MinePoolMapper, MinePool> implements MinePoolService {

    public MinePool getPoolUser(Long id) {
        return super.baseMapper.selectOne(new QueryWrapper<MinePool>().eq("create_user", id).eq("status",1));
    }
}
