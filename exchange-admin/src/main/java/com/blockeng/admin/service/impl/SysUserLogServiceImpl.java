package com.blockeng.admin.service.impl;

import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.dto.SysUserLogDTO;
import com.blockeng.admin.entity.Account;
import com.blockeng.admin.entity.SysUserLog;
import com.blockeng.admin.mapper.SysUserLogMapper;
import com.blockeng.admin.service.SysUserLogService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统日志 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-11
 */
@Service
public class SysUserLogServiceImpl extends ServiceImpl<SysUserLogMapper, SysUserLog> implements SysUserLogService {


    @Override
    public Page<SysUserLogDTO> selectListPage(Page<SysUserLogDTO> page, Wrapper<SysUserLogDTO> wrapper) {
        wrapper = (Wrapper<SysUserLogDTO>) SqlHelper.fillWrapper(page, wrapper);
        page.setRecords(baseMapper.selectListPage(page, wrapper));
        return page;
    }
}
