package com.blockeng.admin.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.dto.MinePoolDTO;
import com.blockeng.admin.entity.MinePool;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 矿池 Mapper 接口
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface MinePoolMapper extends BaseMapper<MinePool> {

    List<MinePoolDTO> selectPoolListPage(Page<MinePoolDTO> page,  @Param("ew")Wrapper<MinePoolDTO> wrapper);
}
