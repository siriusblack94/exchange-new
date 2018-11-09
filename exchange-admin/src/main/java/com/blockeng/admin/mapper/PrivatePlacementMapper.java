package com.blockeng.admin.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.dto.PrivatePlacementDTO;
import com.blockeng.admin.entity.PrivatePlacement;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther: sirius
 * @Date: 2018/8/15 20:29
 * @Description:
 */
public interface PrivatePlacementMapper extends BaseMapper<PrivatePlacement> {

    List<PrivatePlacementDTO> selectListPage(Page<PrivatePlacementDTO> page, @Param("ew") Wrapper<PrivatePlacementDTO> wrapper);
}
