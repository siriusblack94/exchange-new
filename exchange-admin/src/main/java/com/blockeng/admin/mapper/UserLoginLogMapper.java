package com.blockeng.admin.mapper;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.dto.UserCountLoginDTO;
import com.blockeng.admin.entity.UserLoginLog;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户登录日志 Mapper 接口
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
public interface UserLoginLogMapper extends BaseMapper<UserLoginLog> {

    List<UserCountLoginDTO> selectLoginCountPage(Page<UserCountLoginDTO> page, @Param("ew") Wrapper<UserCountLoginDTO> wrapper);

    /**
     * 根据日期统计登陆人数
     *
     * @param countDate
     * @return
     */
    Integer countloginNumByDate(@Param("countDate") String countDate);

    /**
     * 根据日期查询用户ID字符串
     *
     * @param countDate
     * @return
     * @desc '1,2,3'
     */
    String selectUserIdStrsBydate(@Param("countDate") String countDate);

}
