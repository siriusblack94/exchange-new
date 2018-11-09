package com.blockeng.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.admin.entity.SysUserRole;
import com.blockeng.admin.mapper.SysUserRoleMapper;
import com.blockeng.admin.service.SysUserRoleService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户角色配置 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-11
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

    @Override
    public boolean isAdminUser(long userId) {
        return baseMapper.isAdminUser(userId);
    }

    @Override
    public boolean grantRoles(Long userId, List<SysUserRole> sysUserRoles) {
        EntityWrapper<SysUserRole> ew = new EntityWrapper<>();
        ew.eq("role_id", userId);
        super.delete(ew);
        if (CollectionUtils.isNotEmpty(sysUserRoles)) {
            insertBatch(sysUserRoles);
        }
        return true;
    }
}
