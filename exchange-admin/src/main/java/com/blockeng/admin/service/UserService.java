package com.blockeng.admin.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.blockeng.admin.dto.TurnoverOrderCountDTO;
import com.blockeng.admin.dto.UserBlanceTopDTO;
import com.blockeng.admin.dto.UserCountRegDTO;
import com.blockeng.admin.dto.UserDTO;
import com.blockeng.admin.entity.User;
import com.blockeng.admin.entity.UserAuthAuditRecord;
import com.blockeng.framework.constants.Constant;
import com.blockeng.framework.enums.AdminUserType;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
public interface UserService extends IService<User>, Constant {

    /**
     * 获取管理员账户
     *
     * @param adminUserType 管理员类型
     * @return
     */
    User queryAdminUser(AdminUserType adminUserType);

    /**
     * 注册统计
     *
     * @param page
     * @param wrapper
     * @return
     */
    Page<UserCountRegDTO> selectRegCountPage(Page<UserCountRegDTO> page, Wrapper<UserCountRegDTO> wrapper);

    /**
     * 持仓排行
     *
     * @param page
     * @param wrapper
     * @return
     */
    Page<UserBlanceTopDTO> selectBalanceTopPage(Page<UserBlanceTopDTO> page, Wrapper<UserBlanceTopDTO> wrapper);

    /**
     * #持币人数
     *
     * @param coins
     * @return
     */
    List<TurnoverOrderCountDTO> selectUserCount(String[] coins);


    Page<UserDTO> selectListPage(Page<UserDTO> var1, Wrapper<UserDTO> var2);


    int selectAuditAccount(EntityWrapper<UserAuthAuditRecord> ew);


    List<UserDTO> selectListAuditFromAuth(int current, int end, String userAuthStatus, String startTime, String endTime);


    List<UserDTO> selectListAuditFromUser(EntityWrapper<UserDTO> ew);

    Page<UserDTO> selectListAuditPage(Page<UserDTO> var1, Wrapper<UserDTO> ew);


}
