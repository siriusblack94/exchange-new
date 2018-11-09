package com.blockeng.admin.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 平台用户 Mapper 接口
 * </p>
 *
 * @author qiang
 * @since 2018-05-11
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    List<SysUser> selectSysUserPage(Page<SysUser> page, @Param("ew") EntityWrapper<SysUser> ew);
}
