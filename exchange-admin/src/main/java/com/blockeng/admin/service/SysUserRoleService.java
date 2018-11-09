package com.blockeng.admin.service;

import com.blockeng.admin.entity.SysUserRole;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 用户角色配置 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-11
 */
public interface SysUserRoleService extends IService<SysUserRole> {

    boolean isAdminUser(long userId);

    boolean grantRoles(Long userId, List<SysUserRole> sysUserRoles);
}