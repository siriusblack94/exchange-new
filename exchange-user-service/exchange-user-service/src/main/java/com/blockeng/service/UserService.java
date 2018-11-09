package com.blockeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.entity.AuthUser;
import com.blockeng.entity.User;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.security.UserDetails;
import com.blockeng.web.vo.*;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface UserService extends IService<User> {

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return
     */
    User selectByUsername(String username);

    /**
     * 根据手机号查询用户是否已注册
     *
     * @param mobile 手机号
     * @return
     */
    User selectByMobile(String mobile);

    /**
     * 用户注册
     *
     * @param form 注册信息
     * @return
     */
    Response register(RegisterForm form);

    /**
     * 实名认证
     */
    Response updateAuthAccount(UserDetails userDetails, UserAuthInfoForm form);

    /**
     * 修改资金密码
     */
    Response updatePayPassword(UserDetails userDetails, ChangePasswordForm form);

    /**
     * 修改登录密码
     */
    Response updatePassword(UserDetails userDetails, ChangePasswordForm form);

    /**
     * 修改手机号
     */
    Response updatePhone(UserDetails userDetails, ChangePhoneForm form);


    /**
     * 修改邮箱
     */
    Response updateEmail(UserDetails userDetails, ChangeEmailForm form);


    /**
     * 设置资金密码
     */
    Response setPayPassword(UserDetails userDetails, ChangePayPasswordForm form);

    /**
     * 设置密码
     *
     * @param form
     * @return
     */
    Response setPassword(SetPasswordForm form);

    /**
     * 高级实名认证
     *
     * @param userDetails
     * @param
     * @return
     */
    Response authUser(UserDetails userDetails, AuthUser authUser);

    /**
     * 设置用户基本信息
     *
     * @param userDetails
     * @param form
     * @return
     */
    Response setUserBase(UserDetails userDetails, UserBaseForm form);

    /**
     * 查询邀请列表
     *
     * @param userId 用户ID
     * @return
     */
    List<User> selectListByInviteId(long userId);

}
