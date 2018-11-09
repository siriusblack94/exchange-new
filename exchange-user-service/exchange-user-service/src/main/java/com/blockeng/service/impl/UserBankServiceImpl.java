package com.blockeng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.entity.UserBank;
import com.blockeng.mapper.UserBankMapper;
import com.blockeng.service.UserBankService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户人民币提现地址 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
public class UserBankServiceImpl extends ServiceImpl<UserBankMapper, UserBank> implements UserBankService {

}
