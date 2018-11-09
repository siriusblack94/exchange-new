package com.blockeng.admin.service.impl;

import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.admin.dto.PrivatePlacementDTO;
import com.blockeng.admin.entity.PrivatePlacement;
import com.blockeng.admin.mapper.PrivatePlacementMapper;
import com.blockeng.admin.service.PrivatePlacementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrivatePlacementServiceImpl extends ServiceImpl<PrivatePlacementMapper, PrivatePlacement> implements PrivatePlacementService {
    @Autowired
    PrivatePlacementMapper privatePlacementMapper;

    @Override
    public Page<PrivatePlacementDTO> selectListPage(Page<PrivatePlacementDTO> page, Wrapper<PrivatePlacementDTO> wrapper) {
        wrapper = (Wrapper<PrivatePlacementDTO>) SqlHelper.fillWrapper(page, wrapper);
        page.setRecords(baseMapper.selectListPage(page, wrapper));
        return page;
    }

}
