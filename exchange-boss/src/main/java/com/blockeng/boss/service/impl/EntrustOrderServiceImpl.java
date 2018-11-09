package com.blockeng.boss.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.boss.entity.EntrustOrder;
import com.blockeng.boss.mapper.EntrustOrderMapper;
import com.blockeng.boss.service.EntrustOrderService;
import org.springframework.stereotype.Service;

/**
 * @author maple
 * @date 2018/10/26 10:29
 **/
@Service
public class EntrustOrderServiceImpl extends ServiceImpl<EntrustOrderMapper, EntrustOrder> implements EntrustOrderService {

    @Override
    public int cancelEntrustOrder(Long id) {
        return baseMapper.cancelEntrustOrder(id);
    }
}
