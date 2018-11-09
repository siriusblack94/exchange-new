package com.blockeng.boss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.boss.entity.EntrustOrder;

/**
 * @author maple
 * @date 2018/10/26 10:25
 **/
public interface EntrustOrderService  extends IService<EntrustOrder> {


    int cancelEntrustOrder(Long id);
}
