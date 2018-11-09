package com.blockeng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.entity.UserAuthInfo;
import com.blockeng.mapper.UserAuthInfoMapper;
import com.blockeng.service.UserAuthInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 实名认证信息 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
public class UserAuthInfoServiceImpl extends ServiceImpl<UserAuthInfoMapper, UserAuthInfo> implements UserAuthInfoService {

}
