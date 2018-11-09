package com.blockeng.admin.mapper;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.dto.*;
import com.blockeng.admin.entity.User;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.blockeng.admin.entity.UserAuthAuditRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
public interface UserMapper extends BaseMapper<User> {

    List<UserCountRegDTO> selectRegCountPage(Page<UserCountRegDTO> page, @Param("ew") Wrapper<UserCountRegDTO> wrapper);

    /**
     * 根据日期统计用户绑定邮箱人数
     *
     * @param countDate
     * @return
     */
    Integer countEmailBindByDate(@Param("countDate") String countDate);

    /**
     * 根据日期统计用户设置资金密码人数
     *
     * @param countDate
     * @return
     */
    Integer countSetPayPwdByDate(@Param("countDate") String countDate);

    /**
     * 根据日期查询用户ID字符串
     *
     * @param countDate
     * @return
     * @desc '1,2,3'
     */
    String selectIdStrBydate(@Param("countDate") String countDate);

    /**
     * 用户持仓排行
     *
     * @param page
     * @param wrapper
     * @return
     */
    List<UserBlanceTopDTO> selectBalanceTopPage(Page<UserBlanceTopDTO> page, @Param("ew") Wrapper<UserBlanceTopDTO> wrapper);

    /**
     * #持币人数
     *
     * @param coins
     * @return
     */
    List<TurnoverOrderCountDTO> selectUserCount(@Param("coins") String[] coins);


    List<UserDTO> selectListPage(Page<UserDTO> page, @Param("ew") Wrapper<UserDTO> wrapper);


    Integer selectAuditAccount(
            @Param("ew") EntityWrapper<UserAuthAuditRecord> ew);


    List<UserDTO> selectListAuditFromAuth(@Param("current") int current,
                                          @Param("end") int end,
                                          @Param("userAuthStatus") String userAuthStatus,
                                          @Param("startTime") String startTime,
                                          @Param("endTime") String endTime);


    List<UserDTO> selectListAuditByUser(@Param("ew") Wrapper<UserDTO> wrapper);

    List<UserDTO> selectListAuditPage(Page<UserDTO> page, @Param("ew") Wrapper<UserDTO> wrapper);
}
