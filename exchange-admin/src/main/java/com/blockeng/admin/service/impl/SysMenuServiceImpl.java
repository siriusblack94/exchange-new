package com.blockeng.admin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.admin.entity.SysMenu;
import com.blockeng.admin.mapper.SysMenuMapper;
import com.blockeng.admin.service.SysMenuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 系统菜单 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-11
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Override
    public List<SysMenu> selectPrivilegeMenuList(Long roleId, Long userId) {
        return baseMapper.selectPrivilegeMenuList(roleId,userId);
    }

    @Override
    public List<SysMenu> selectListByUserId(Long userId) {
        return baseMapper.selectListByUserId(userId);
    }

    @Override
    public List<SysMenu> childs(List<SysMenu> menus) {
        List<SysMenu> rtnList = new ArrayList<>();//顶级菜单个数（按组）
        for (SysMenu menu : menus) {
            if (menu.getParentId() == null) {    //如果是顶级菜单
                menu.setChilds(handleSubMenu(menus, menu.getId()));//递归生成子菜单
                rtnList.add(menu);
            }
        }
        return rtnList;
    }

    @Override
    public List<String> strings(List<SysMenu> menus) {
        List<String> rtnList = new ArrayList<>();
        for (SysMenu menu : menus) {
            rtnList.add(menu.getMenuKey());
        }
        return rtnList;
    }

    private static List<SysMenu> handleSubMenu(List<SysMenu> menuList, Long id) {
        List<SysMenu> rtnlist = new ArrayList<SysMenu>();
        for (SysMenu sysMenu : menuList) {
            if (id != null && sysMenu.getParentId() != null && sysMenu.getParentId().equals(id)) {
                SysMenu menu = new SysMenu();
                menu.setId(sysMenu.getId());
                menu.setParentId(sysMenu.getParentId());
                menu.setName(sysMenu.getName());
                menu.setChilds(handleSubMenu(menuList, sysMenu.getId()));
                rtnlist.add(menu);
            }
        }
        return rtnlist;
    }
}
