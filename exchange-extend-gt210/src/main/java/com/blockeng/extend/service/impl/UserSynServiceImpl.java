package com.blockeng.extend.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.extend.entity.UserSyn;
import com.blockeng.extend.mapper.UserSynMapper;
import com.blockeng.extend.service.UserSynService;
import org.springframework.stereotype.Service;
/**
 * @Desc 同步用户信息接口
 * @author shadow
 * @created 2018/10/25
 */

@Service
public class UserSynServiceImpl extends ServiceImpl<UserSynMapper,UserSyn> implements UserSynService {
}

