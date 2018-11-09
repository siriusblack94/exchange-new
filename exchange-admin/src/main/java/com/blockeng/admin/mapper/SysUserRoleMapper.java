package com.blockeng.admin.mapper;

import com.blockeng.admin.entity.SysUserRole;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 * 用户角色配置 Mapper 接口
 * </p>
 *
 * @author qiang
 * @since 2018-05-11
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    boolean isAdminUser(long userId);
}
