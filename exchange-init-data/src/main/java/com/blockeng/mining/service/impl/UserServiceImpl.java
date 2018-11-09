package com.blockeng.mining.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.blockeng.mining.entity.User;
import com.blockeng.mining.mapper.UserMapper;
import com.blockeng.mining.service.UserService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User selectByMobile(String mobile) {
        QueryWrapper<User> ew = new QueryWrapper<>();
        ew.eq("mobile", mobile);
        ew.or().eq("email", mobile);
        return super.selectOne(ew);
    }

}
