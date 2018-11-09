package com.blockeng.admin.web.vo.mappers;

import com.blockeng.admin.entity.SysUserRole;
import com.blockeng.admin.web.vo.GrantRolesForm;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qiang
 */
public class GrantRolesMapper {

    public static List<SysUserRole> convertRoleList(GrantRolesForm form) {
        List<SysUserRole> sysUserRoles = new ArrayList<>();
        for (Long roleId : form.getRoleIds()) {
            sysUserRoles.add(new SysUserRole()
                    .setRoleId(roleId)
                    .setUserId(form.getUserId())
            );
        }
        return sysUserRoles;
    }
}