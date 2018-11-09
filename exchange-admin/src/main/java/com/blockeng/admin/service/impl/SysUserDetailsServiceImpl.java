package com.blockeng.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.blockeng.admin.entity.SysMenu;
import com.blockeng.admin.entity.SysPrivilege;
import com.blockeng.admin.entity.SysUser;
import com.blockeng.admin.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qiang
 */
@Service
public class SysUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysPrivilegeService sysPrivilegeService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        EntityWrapper<SysUser> ew = new EntityWrapper<>();
        ew.eq("username", username);
        SysUser user = sysUserService.selectOne(ew);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }
        // 查询权限信息
        List<SysPrivilege> privileges;
        List<SysMenu> menus;
        // 如果是管理员，加载所有权限和菜单
//        if (sysUserRoleService.isAdminUser(user.getId())) {
//            privileges = sysPrivilegeService.selectList(new EntityWrapper<>());
//            EntityWrapper<SysMenu> wrapper = new EntityWrapper<>();
//            wrapper.orderBy("id", true);
//            menus = sysMenuService.selectList(wrapper);
//        } else {
            privileges = sysPrivilegeService.selectListByUserId(user.getId());
            menus = sysMenuService.selectListByUserId(user.getId());
            // List<String> ids = new ArrayList<>();
            // menus.forEach(m -> {
            //     ids.add(m.getParentKey());
            // });
            // EntityWrapper<SysMenu> sysMenuEntityWrapper = new EntityWrapper<>();
            // sysMenuEntityWrapper.in("id", ids).orderBy("id", true);
            // List<SysMenu> parentSysMenuList = sysMenuService.selectList(sysMenuEntityWrapper);
            // menus.addAll(0, parentSysMenuList);
//        }
        user.setMenus(menus);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (SysPrivilege privilege : privileges) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(privilege.getName());
            grantedAuthorities.add(grantedAuthority);
        }
        user.setAuthorities(grantedAuthorities);
        return user;
    }
}
