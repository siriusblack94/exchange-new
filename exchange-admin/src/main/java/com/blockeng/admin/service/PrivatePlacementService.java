package com.blockeng.admin.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.blockeng.admin.dto.PrivatePlacementDTO;
import com.blockeng.admin.dto.SysUserLogDTO;
import com.blockeng.admin.entity.PrivatePlacement;

import java.util.List;
import java.util.Map;


/**
 * @Auther: sirius
 * @Date: 2018/8/15 13:15
 * @Description:私募服务类
 */
public interface PrivatePlacementService extends IService<PrivatePlacement> {
    Page<PrivatePlacementDTO> selectListPage(Page<PrivatePlacementDTO> var1, Wrapper<PrivatePlacementDTO> var2);
}
