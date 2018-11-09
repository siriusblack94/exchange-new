package com.blockeng.admin.mapper;

import com.blockeng.admin.entity.SysMenu;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统菜单 Mapper 接口
 * </p>
 *
 * @author qiang
 * @since 2018-05-11
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> selectPrivilegeMenuList(@Param("roleId") Long roleId,@Param("userId") Long userId);

    List<SysMenu> selectListByUserId(Long userId);
}
