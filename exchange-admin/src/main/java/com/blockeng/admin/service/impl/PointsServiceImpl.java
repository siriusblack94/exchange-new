package com.blockeng.admin.service.impl;


import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.admin.entity.Points;
import com.blockeng.admin.mapper.PointsMapper;
import com.blockeng.admin.service.PointsService;



import org.springframework.stereotype.Service;

/**
 * @Auther: sirius
 * @Date: 2018/10/25 11:17
 * @Description:
 */
@Service
public class PointsServiceImpl extends ServiceImpl<PointsMapper, Points> implements PointsService {


    @Override
    public Page<Points> selectListPage(Page<Points> page, Wrapper<Points> qw) {
        qw = (Wrapper<Points>) SqlHelper.fillWrapper(page, qw);
        page.setRecords(baseMapper.selectListPage(page, qw));
        return page;
    }
}
