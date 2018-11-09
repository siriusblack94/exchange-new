package com.blockeng.admin.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.entity.Points;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther: sirius
 * @Date: 2018/10/25 11:18
 * @Description:
 */
public interface PointsMapper extends BaseMapper<Points> {
    List<Points> selectListPage(Page<Points> page,  @Param("ew")Wrapper<Points> qw);
}
