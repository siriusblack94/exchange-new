package com.blockeng.admin.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.entity.SysPrivilege;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 权限配置 Mapper 接口
 * </p>
 *
 * @author qiang
 * @since 2018-05-11
 */
public interface SysPrivilegeMapper extends BaseMapper<SysPrivilege> {


    List<SysPrivilege> selectPageByUserId(@Param("current") int current,@Param("size") int size,@Param("userId") Long id);

    long selectCountByUserId(Long id);

    List<SysPrivilege> selectListByUserId(Long userId);
}
