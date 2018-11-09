package com.blockeng.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.blockeng.admin.common.CommonUtils;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.dto.CoinDTO;
import com.blockeng.admin.entity.Account;
import com.blockeng.admin.entity.Coin;
import com.blockeng.admin.entity.CoinConfig;
import com.blockeng.admin.entity.User;
import com.blockeng.admin.mapper.CoinMapper;
import com.blockeng.admin.service.AccountService;
import com.blockeng.admin.service.CoinConfigService;
import com.blockeng.admin.service.CoinService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.admin.service.UserService;
import com.blockeng.framework.constants.Constant;
import com.blockeng.framework.exception.ExchangeException;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 币种配置信息 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Slf4j
@Service
@Transactional
public class CoinServiceImpl extends ServiceImpl<CoinMapper, Coin> implements CoinService {

    private static final int defaultSize = 100;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoinConfigService coinConfigService;

    @Override
    @Async
    public void ansyUpdateAccounts(Long coinId) {
        Integer[] ustyps = {CommonUtils.USER_TYPE_0, CommonUtils.USER_TYPE_1};
        List<User> userList = userService.selectList(new EntityWrapper<User>().in("type", ustyps));
        if (userList != null && userList.size() > 0) {
            List<List<User>> rsList = Lists.partition(userList, defaultSize);
            for (List<User> users : rsList) {
                List<Account> accoutUserIds = new ArrayList<>();
                for (User us : users) {
                    Account account = new Account();
                    account.setUserId(us.getId());
                    account.setStatus(CommonUtils.STATUS_1);
                    account.setCoinId(coinId);
                    account.setWithdrawalsAmount(BigDecimal.ZERO);
                    account.setBalanceAmount(BigDecimal.ZERO);
                    account.setFreezeAmount(BigDecimal.ZERO);
                    account.setRechargeAmount(BigDecimal.ZERO);
                    account.setNetValue(BigDecimal.ZERO);
                    account.setLockMargin(BigDecimal.ZERO);
                    account.setFloatProfit(BigDecimal.ZERO);
                    account.setTotalProfit(BigDecimal.ZERO);
                    account.setVersion(0L);
                    accoutUserIds.add(account);
                }
                if (accoutUserIds != null) {
                    accountService.insertBatch(accoutUserIds);
                }
            }
        }
    }

    @Override
    public ResultMap insertCoin(Coin coin) {
        EntityWrapper<Coin> ew = new EntityWrapper<>();
        ew.eq("name", coin.getName());
        if (null != selectOne(ew)) {
            return ResultMap.getFailureResult("该名称已添加");
        }
        if ("rgb".equalsIgnoreCase(coin.getWallet()))//如果是认购币关闭充值提现
            coin.setRechargeFlag(0).setWithdrawFlag(0);
        if (!insert(coin)) {
            throw new ExchangeException("币种新增失败!");
        }
        //异步初始化
        ansyUpdateAccounts(coin.getId());
        // 如果是Qbb类型，需要把数据写到 coin_config
        if ("qbb".equalsIgnoreCase(coin.getWallet())) {
            CoinConfig coinConfig = new CoinConfig()
                    .setId(coin.getId())
                    .setCoinType(coin.getType())
                    .setName(coin.getName());
            if (!coinConfigService.insert(coinConfig))
                throw new ExchangeException("QBB操作失败!");
        }
        return ResultMap.getSuccessfulResult(new CoinDTO().setCoinId(coin.getId()));
    }

    @Override
    public ResultMap updateCoin(Coin coin) {
        if ("rgb".equalsIgnoreCase(coin.getWallet()))//如果是认购币关闭充值提现
            coin.setRechargeFlag(0).setWithdrawFlag(0);
        if (updateById(coin)) {
            if ("qbb".equalsIgnoreCase(coin.getWallet())) {//钱包币
                if (!coinConfigService.insertOrUpdate(new CoinConfig()
                        .setId(coin.getId())
                        .setCoinType(coin.getType())
                        .setName(coin.getName())))
                    throw new ExchangeException("QBB操作失败!");
            }
            return ResultMap.getSuccessfulResult(new CoinDTO().setCoinId(coin.getId()));
        } else {
            throw new ExchangeException("更新操作失败!");
        }
    }

    @Override
    public boolean updateConfig(Coin newCoin, CoinConfig coinConfig) {
        return this.updateById(newCoin)&&coinConfigService.updateById(coinConfig);
    }

    @Override
    public List<Coin> getAllValidCoin() {

        EntityWrapper<Coin> ew = new EntityWrapper<Coin>();
        //0:禁用 1:有效
        String status="1";
        ew.eq("status", status);
        List<Coin> coinList=baseMapper.selectList(ew);
        return coinList;
    }

}
