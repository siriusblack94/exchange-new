package com.blockeng.admin.service;

import com.baomidou.mybatisplus.service.IService;
import com.blockeng.admin.entity.SysRoleMenu;
import com.blockeng.admin.entity.SysRolePrivilege;

import java.util.List;

/**
 * <p>
 * 角色权限配置 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-11
 */
public interface SysRolePrivilegeService extends IService<SysRolePrivilege> {

    /**
     * 赋予权限
     *
     * @param roleId
     * @param sysRolePrivileges
     * @return
     */
    boolean grantPrivileges(Long roleId, List<SysRolePrivilege> sysRolePrivileges, List<SysRoleMenu> sysRoleMenus);
}
