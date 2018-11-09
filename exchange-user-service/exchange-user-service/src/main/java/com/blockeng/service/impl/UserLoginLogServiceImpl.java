package com.blockeng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.entity.UserLoginLog;
import com.blockeng.mapper.UserLoginLogMapper;
import com.blockeng.service.UserLoginLogService;
import org.springframework.stereotype.Service;

/**
 * @author maple
 * @date 2018/10/25 16:52
 **/
@Service
public class UserLoginLogServiceImpl  extends ServiceImpl<UserLoginLogMapper, UserLoginLog> implements UserLoginLogService {

}
