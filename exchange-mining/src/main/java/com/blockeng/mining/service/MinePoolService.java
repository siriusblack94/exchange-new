package com.blockeng.mining.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.mining.entity.MinePool;


/**
 * <p>
 * 矿池 数据查询
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */

public interface MinePoolService extends IService<MinePool> {
    MinePool getPoolUser(Long id);
}
