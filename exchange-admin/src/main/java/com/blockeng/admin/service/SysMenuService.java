package com.blockeng.admin.service;

import com.baomidou.mybatisplus.service.IService;
import com.blockeng.admin.entity.SysMenu;

import java.util.List;

/**
 * <p>
 * 系统菜单 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-11
 */
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> selectPrivilegeMenuList(Long roleId, Long userId);

    List<SysMenu> selectListByUserId(Long userId);

    List<SysMenu> childs(List<SysMenu> menus);

    List<String> strings(List<SysMenu> menus);
}
