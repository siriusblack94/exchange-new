package com.blockeng.admin.service.impl;

import com.blockeng.admin.entity.UserAuthInfo;
import com.blockeng.admin.mapper.UserAuthInfoMapper;
import com.blockeng.admin.service.UserAuthInfoService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 实名认证信息 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Service
public class UserAuthInfoServiceImpl extends ServiceImpl<UserAuthInfoMapper, UserAuthInfo> implements UserAuthInfoService {

}
