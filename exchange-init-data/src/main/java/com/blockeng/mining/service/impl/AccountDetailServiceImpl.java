package com.blockeng.mining.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.mining.entity.AccountDetail;
import com.blockeng.mining.mapper.AccountDetailMapper;
import com.blockeng.mining.service.AccountDetailService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 资金账户流水 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-16
 */
@Service
public class AccountDetailServiceImpl extends ServiceImpl<AccountDetailMapper, AccountDetail> implements AccountDetailService {

}
