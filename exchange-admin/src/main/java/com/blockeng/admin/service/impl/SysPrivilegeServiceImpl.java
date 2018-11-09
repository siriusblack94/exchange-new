package com.blockeng.admin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.entity.SysPrivilege;
import com.blockeng.admin.mapper.SysPrivilegeMapper;
import com.blockeng.admin.service.SysPrivilegeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 权限配置 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-11
 */
@Service
public class SysPrivilegeServiceImpl extends ServiceImpl<SysPrivilegeMapper, SysPrivilege> implements SysPrivilegeService {

    @Override
    public List<SysPrivilege> selectListByUserId(Long userId) {
        return baseMapper.selectListByUserId(userId);
    }

    @Override
    public List<SysPrivilege> selectPageByUserId(Page<SysPrivilege> page, Long id) {
        return baseMapper.selectPageByUserId(page.getCurrent(),page.getSize(),id);
    }

    @Override
    public long selectCountByUserId(Long id) {
        return baseMapper.selectCountByUserId(id);
    }
}
