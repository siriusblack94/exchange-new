package com.blockeng.mining.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.mining.entity.User;


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
     * 根据手机号查询用户是否已注册
     *
     * @param mobile 手机号
     * @return
     */
    User selectByMobile(String mobile);



}
