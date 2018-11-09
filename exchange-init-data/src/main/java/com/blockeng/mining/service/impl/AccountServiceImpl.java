package com.blockeng.mining.service.impl;



import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.mining.entity.Account;
import com.blockeng.mining.mapper.AccountMapper;
import com.blockeng.mining.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


/**
 * <p>
 * 用户财产记录 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {
    @Autowired
    private AccountMapper accountMapper;

    @Override
    public int addAmount(long accountId, BigDecimal amount) {
        return accountMapper.addAmount(accountId,amount);
    }
}
