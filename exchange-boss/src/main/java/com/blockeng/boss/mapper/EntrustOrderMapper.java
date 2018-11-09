package com.blockeng.boss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockeng.boss.entity.EntrustOrder;

public interface EntrustOrderMapper extends BaseMapper<EntrustOrder> {

    int cancelEntrustOrder(Long id);
}
