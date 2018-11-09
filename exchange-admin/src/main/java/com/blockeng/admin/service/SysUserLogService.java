package com.blockeng.admin.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.dto.SysUserLogDTO;
import com.blockeng.admin.entity.SysUserLog;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 系统日志 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-11
 */
public interface SysUserLogService extends IService<SysUserLog> {

    /**
     * 分页查询
     *
     * @param var1
     * @param var2
     * @return
     */
    Page<SysUserLogDTO> selectListPage(Page<SysUserLogDTO> var1, Wrapper<SysUserLogDTO> var2);

}
