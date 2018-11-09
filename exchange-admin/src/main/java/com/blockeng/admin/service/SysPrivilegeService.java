package com.blockeng.admin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.entity.SysPrivilege;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 权限配置 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-11
 */
public interface SysPrivilegeService extends IService<SysPrivilege> {

    List<SysPrivilege> selectListByUserId(Long userId);


    List<SysPrivilege> selectPageByUserId(Page<SysPrivilege> page, Long id);

    long selectCountByUserId(Long id);

}
