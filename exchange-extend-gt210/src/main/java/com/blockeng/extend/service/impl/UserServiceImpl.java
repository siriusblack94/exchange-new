package com.blockeng.extend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.extend.entity.User;
import com.blockeng.extend.mapper.UserMapper;
import com.blockeng.extend.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @Auther: sirius
 * @Date: 2018/10/24 16:24
 * @Description:
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
