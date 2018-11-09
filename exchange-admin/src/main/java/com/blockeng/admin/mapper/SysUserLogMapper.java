package com.blockeng.admin.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.dto.SysUserLogDTO;
import com.blockeng.admin.entity.SysUserLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统日志 Mapper 接口
 * </p>
 *
 * @author qiang
 * @since 2018-05-11
 */
public interface SysUserLogMapper extends BaseMapper<SysUserLog> {

    List<SysUserLogDTO> selectListPage(Page<SysUserLogDTO> page, @Param("ew") Wrapper<SysUserLogDTO> wrapper);

}
