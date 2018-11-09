package com.blockeng.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.admin.entity.SysRoleMenu;
import com.blockeng.admin.entity.SysRolePrivilege;
import com.blockeng.admin.mapper.SysRolePrivilegeMapper;
import com.blockeng.admin.service.SysRoleMenuService;
import com.blockeng.admin.service.SysRolePrivilegeService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 角色权限配置 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-11
 */
@Service
@Transactional
public class SysRolePrivilegeServiceImpl extends ServiceImpl<SysRolePrivilegeMapper, SysRolePrivilege> implements SysRolePrivilegeService {

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Override
    public boolean grantPrivileges(Long roleId, List<SysRolePrivilege> sysRolePrivileges, List<SysRoleMenu> sysRoleMenus) {
        EntityWrapper<SysRolePrivilege> ew = new EntityWrapper<>();
        ew.eq("role_id", roleId);
        super.delete(ew);
        EntityWrapper<SysRoleMenu> wrapper = new EntityWrapper<>();
        wrapper.eq("role_id", roleId);
        sysRoleMenuService.delete(wrapper);
        if (CollectionUtils.isNotEmpty(sysRolePrivileges)) {
            insertBatch(sysRolePrivileges);
        }
        if (CollectionUtils.isNotEmpty(sysRoleMenus)) {
            sysRoleMenuService.insertBatch(sysRoleMenus);
        }
        return true;
    }
}
