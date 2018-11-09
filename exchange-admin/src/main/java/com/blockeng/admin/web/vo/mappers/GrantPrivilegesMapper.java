package com.blockeng.admin.web.vo.mappers;

import com.blockeng.admin.entity.SysMenu;
import com.blockeng.admin.entity.SysPrivilege;
import com.blockeng.admin.entity.SysRoleMenu;
import com.blockeng.admin.entity.SysRolePrivilege;
import com.blockeng.admin.service.SysMenuService;
import com.blockeng.admin.service.SysPrivilegeService;
import com.blockeng.admin.web.vo.GrantPrivilegesForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author qiang
 */
@Component
public class GrantPrivilegesMapper {

    // public static List<SysRolePrivilege> convertPrivilegeList(GrantPrivilegesForm form) {
    //     List<SysRolePrivilege> sysRolePrivileges = new ArrayList<>();
    //     for (Long privilegeId : form.getPrivilegeIds()) {
    //         sysRolePrivileges.add(new SysRolePrivilege()
    //                 .setRoleId(form.getRoleId())
    //                 .setPrivilegeId(privilegeId));
    //     }
    //     return sysRolePrivileges;
    // }

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysPrivilegeService sysPrivilegeService;

    public List<Object> convertPrivilegeList(GrantPrivilegesForm form) {
        List<Object> data = new ArrayList<>(2);
        List<SysRolePrivilege> sysRolePrivileges = new ArrayList<>();
        List<SysRoleMenu> sysRoleMenus = new ArrayList<>();
        data.add(sysRolePrivileges);
        data.add(sysRoleMenus);
        Set<Long> menuIds = new HashSet<>();
        for (Long privilegeId : form.getPrivilegeIds()) {
            sysRolePrivileges.add(new SysRolePrivilege()
                    .setRoleId(form.getRoleId())
                    .setPrivilegeId(privilegeId));
            menuIds.addAll(this.getMenuId(privilegeId));
        }
        for (Long menuId : menuIds) {
            sysRoleMenus.add(new SysRoleMenu()
                    .setRoleId(form.getRoleId())
                    .setMenuId(menuId));
        }
        return data;
    }

    private Set<Long> getMenuId(Long privilegeId) {
        Set<Long> menuIds = new HashSet<>();
        SysPrivilege sysPrivilege = sysPrivilegeService.selectById(privilegeId);
        if (sysPrivilege != null) {
            menuIds.add(sysPrivilege.getMenuId());
            // 添加上级菜单ID
            menuIds.addAll(this.getParentMenuId(sysPrivilege.getMenuId(), menuIds));
        }
        return menuIds;
    }

    /**
     * @param menuId
     * @param menuIds
     * @return
     */
    private Set<Long> getParentMenuId(Long menuId, Set<Long> menuIds) {
        SysMenu sysMenu = sysMenuService.selectById(menuId);
        if (sysMenu != null && sysMenu.getParentId() != null && sysMenu.getParentId() > 0) {
            menuIds.add(sysMenu.getParentId());
            return getParentMenuId(sysMenu.getParentId(), menuIds);
        } else {
            return menuIds;
        }
    }
}