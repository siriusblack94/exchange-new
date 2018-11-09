package com.blockeng.mining.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.mining.entity.User;

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


    List<User> authStatusList();

    List<User> inviteList();

}
