package com.blockeng.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.dto.CoinTransferDTO;
import com.blockeng.entity.Account;
import com.blockeng.entity.CoinTransfer;
import com.blockeng.framework.enums.BusinessType;
import com.blockeng.framework.exception.AccountException;
import com.blockeng.mapper.CoinTransferMapper;
import com.blockeng.service.AccountService;
import com.blockeng.service.CoinService;
import com.blockeng.service.CoinTransferService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author: jakiro
 * @Date: 2018-10-30 14:21
 * @Description: 站内转帐
 */
@Service
@Slf4j
public class CoinTransferServiceImpl extends ServiceImpl<CoinTransferMapper,CoinTransfer> implements CoinTransferService {

    @Autowired
    AccountService accountService;

    @Autowired
    CoinService coinService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean transferAccounts(Long moneyMakerUserId, Long coinId, BigDecimal num, Long payeeUserId) {

        //拿到打款人账户信息
        Account account_makeMoney=accountService.queryByUserIdAndCoinId(moneyMakerUserId,coinId);
        //判断打款人账户信息是否存在
        if(account_makeMoney==null){
            throw new com.blockeng.framework.exception.AccountException("打款人信息不存在");
        }
        //先判断收款人 帐号是否存在
        Account account_payee=accountService.queryByUserIdAndCoinId(payeeUserId,coinId);
        if(account_payee==null){
            throw new com.blockeng.framework.exception.AccountException("收款人账户不存在");
        }
        //先拿到打款人对应的币种账户信息
        if(account_makeMoney.getBalanceAmount().compareTo(num)==-1){
            //说明余额比扣款要少
            throw new AccountException("余额不足");
        }
        CoinTransfer coinTransfer=new CoinTransfer();
        coinTransfer.setMoneyMakerUserId(moneyMakerUserId).setNum(num).setPayeeUserId(payeeUserId).setStatus(1)
                .setCreated(new Date()).setLastUpdateTime(new Date()).setCoinId(coinId);

        //站内转帐明细表
        baseMapper.saveCoinTransfer(coinTransfer);
        accountService.subtractBalanceAmount(moneyMakerUserId,coinId,num,BusinessType.COIN_TRANSFER,"站内转帐",coinTransfer.getId());
        accountService.addAmount(payeeUserId,coinId,num,BusinessType.COIN_TRANSFER,"站内转帐",coinTransfer.getId());

        return true;
    }

    /**
     * 站内转帐记录 transferMethod:0:全部 1:转出 2:转入
     * */
    @Override
    public List<CoinTransferDTO> getCoinTransferDetail(int current, int size,String startTime,String endTime,Long coinId,Long userId,int transferMethod) {

        var listCoinTransferDTO=new ArrayList<CoinTransferDTO>();
        var coin=coinService.queryById(coinId);
        var pager = new Page<CoinTransfer>(current, size);
        var paramMap=new HashMap<String,Object>();

        if (StringUtils.isNotBlank(endTime)) {
            endTime = endTime + " 23:59:59";
        }

        paramMap.put("startTime",startTime);
        paramMap.put("endTime",endTime);
        paramMap.put("coinId",coinId);
        paramMap.put("userId",userId);

        switch (transferMethod){
            case 0://全部
                paramMap.put("transferMethod","all");
                break;
            case 1://转出
                paramMap.put("transferMethod","out");
                break;
            case 2://转入
                paramMap.put("transferMethod","in");
                break;
        }

        List<CoinTransfer> coinTransferList=baseMapper.selectCoinTransferDetail(pager,paramMap);

        coinTransferList.forEach(e->{
            var coinTransferDTO=new CoinTransferDTO();
            coinTransferDTO.setDate(e.getCreated());
            //1.转出 2.转入
            if(e.getMoneyMakerUserId().equals(String.valueOf(userId))){
                coinTransferDTO.setDirection(1);
            }else if(e.getPayeeUserId().equals(String.valueOf(userId))){
                coinTransferDTO.setDirection(2);
            }else{
                return;
            }
            coinTransferDTO.setTransferNum(e.getNum());
            coinTransferDTO.setCoinName(coin.getName());
            listCoinTransferDTO.add(coinTransferDTO);
        });
        return listCoinTransferDTO;
    }
}
