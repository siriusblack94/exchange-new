package com.blockeng.admin.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.blockeng.admin.entity.SysUser;

/**
 * <p>
 * 平台用户 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-11
 */
public interface SysUserService extends IService<SysUser> {

    SysUser login(String username, String password);

    String refreshToken(String oldToken);

    Page<SysUser> selectSysUserPage(Page<SysUser> page, EntityWrapper<SysUser> ew);
}
