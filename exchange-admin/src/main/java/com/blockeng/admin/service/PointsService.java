package com.blockeng.admin.service;


import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.blockeng.admin.entity.Points;


/**
 * @Auther: sirius
 * @Date: 2018/10/25 11:14
 * @Description:
 */
public interface PointsService extends IService<Points> {


    Page<Points> selectListPage(Page<Points> page, Wrapper<Points> qw);
}
