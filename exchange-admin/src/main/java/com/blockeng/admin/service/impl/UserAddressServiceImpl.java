package com.blockeng.admin.service.impl;

import com.blockeng.admin.entity.UserAddress;
import com.blockeng.admin.mapper.UserAddressMapper;
import com.blockeng.admin.service.UserAddressService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户以太系列钱包地址信息 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {

}
