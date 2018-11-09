package com.blockeng.boss.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.boss.entity.AccountDetail;
import com.blockeng.boss.mapper.AccountDetailMapper;
import com.blockeng.boss.service.AccountDetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 资金账户流水 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-16
 */
@Service
@Transactional
public class AccountDetailServiceImpl extends ServiceImpl<AccountDetailMapper, AccountDetail> implements AccountDetailService {

}
