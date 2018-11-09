package com.blockeng.admin.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.blockeng.admin.dto.MinePoolDTO;
import com.blockeng.admin.entity.MinePool;

/**
 * <p>
 * 矿池 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface MinePoolService extends IService<MinePool> {

    Page<MinePoolDTO> getPoolListPage(Page<MinePoolDTO> page,Wrapper<MinePoolDTO> wrapper);
}
