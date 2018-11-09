package com.blockeng.mining.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.mining.entity.Account;
import com.blockeng.mining.mapper.AccountMapper;
import com.blockeng.mining.service.AccountService;
import com.blockeng.mining.service.MineHelpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 用户财产记录 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
@Slf4j
@Transactional
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Autowired
    private MineHelpService mineHelpService;

    public Account getMineCoinInfo(Long userId) {
        Long mineCoinId = mineHelpService.getMineCoinId();
        QueryWrapper<Account> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);
        qw.eq("coin_id", mineCoinId);
        Account account = super.baseMapper.selectOne(qw);
        return account;
    }

    @Override
    public BigDecimal selectTotal(Long coinId) {
        return super.baseMapper.selectTotal( coinId);
    }

    @Override
    public List<Account> selectListByFlag() {
        return baseMapper.selectListByFlag();
    }
}
