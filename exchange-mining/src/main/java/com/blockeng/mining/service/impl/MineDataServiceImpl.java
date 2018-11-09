package com.blockeng.mining.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.mining.entity.*;
import com.blockeng.mining.mapper.MineDataMapper;
import com.blockeng.mining.service.AccountService;
import com.blockeng.mining.service.MineDataService;
import com.blockeng.mining.service.MiningDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 矿池数据查询
 * @Author: Chen Long
 * @Date: Created in 2018/6/29 下午4:00
 * @Modified by: Chen Long
 */
@Slf4j
@Service
public class MineDataServiceImpl extends ServiceImpl<MineDataMapper, MineData> implements MineDataService {
    @Autowired
    private AccountService accountService;
    @Autowired
    private MiningDetailService miningDetailService;

    @Override
    public void change() {

        HashMap<Long, BigDecimal> map = new HashMap<>();
        map.put(1016312636281167878L,new BigDecimal(1));
        map.put(1016312636281167879L,new BigDecimal(6186.65));
        map.put(1016312636281167880L,new BigDecimal(190.874));
        map.put(1023188768347467778L,new BigDecimal(0.011036));
        HashMap<String, BigDecimal> map1 = new HashMap<>();
        map1.put("USDT",new BigDecimal(1));
        map1.put("BTC",new BigDecimal(6186.65));
        map1.put("ETH",new BigDecimal(190.874));
        HashMap<String, Long> map2 = new HashMap<>();
        map2.put("USDT",1016312636281167878L);
        map2.put("BTC",1016312636281167879L);
        map2.put("ETH",1016312636281167880L);
        map2.put("HB",1023188768347467778L);
        BigDecimal HB2USDT = new BigDecimal(0.011036);
        BigDecimal rate = new BigDecimal(0.5);

        QueryWrapper<Account> ew = new QueryWrapper<>();
        List<Account> oldAccounts = accountService.selectList(ew);
        Map<Long, List<Account>> accountUserMap = oldAccounts.stream().collect(Collectors.groupingBy(Account::getUserId));
        QueryWrapper<MiningDetail> ew1 = new QueryWrapper<>();
        ew.gt("total_fee", 0);
        List<MiningDetail> oldMiningDetails = miningDetailService.selectList(ew1);
        Map<Long, List<MiningDetail>> miningUserMap = oldMiningDetails.stream().collect(Collectors.groupingBy(MiningDetail::getUserId));

        Map<Long,Account> accounts= getAccount(map);
        Map<Long,MiningDetail> miningDetails= getMiningDetail(map1);
        for (Account oldAccount : oldAccounts) {
            Account account = accounts.get(oldAccount.getUserId());
            MiningDetail miningDetail = miningDetails.get(oldAccount.getUserId());
            if (miningDetail==null||account==null){
//                log.info("账户不存在------"+oldAccount.getUserId());
                continue;
            }
            if(miningDetail.getTotalFee()==null||account.getBalanceAmount()==null){
//                log.info("余额或资产为0------"+oldAccount.getUserId());
                continue;
            }
//            BigDecimal realBalance = account.getBalanceAmount().subtract(miningDetail.getTotalFee());
//            if (realBalance.compareTo(BigDecimal.ZERO)<0){
//                log.info("资产不足手续费------"+oldAccount.getUserId());
//                continue;
//            }
            if(miningDetail.getTotalFee().compareTo(account.getBalanceAmount().multiply(rate))>=0)
            {
                oldAccount.setBalanceAmount(oldAccount.getBalanceAmount().multiply(rate));
//               log.info("大于50%--资产减半----"+oldAccount.getUserId());
                if(oldAccount.getCoinId().compareTo(map2.get("HB"))==0){
                    BigDecimal totalHB= account.getBalanceAmount().multiply(rate)
                            .divide(HB2USDT,8,RoundingMode.HALF_UP)
                            .multiply(new BigDecimal(1.2)).setScale(8,RoundingMode.HALF_UP);
                    oldAccount.setBalanceAmount(oldAccount.getBalanceAmount().add(totalHB));
//                  log.info("大于50%--HB----"+oldAccount.getUserId());
                }
            }else {
               log.info("小于50%------"+oldAccount.getUserId());

                List<Account> accounts1 = accountUserMap.get(oldAccount.getUserId());
                Map<Long,BigDecimal> accountMap = new HashMap<>();
                Map<Long,BigDecimal> miningMap = new HashMap<>();
                List<MiningDetail> miningDetails1 = miningUserMap.get(oldAccount.getUserId());
                for (Account account1 : accounts1) {
                    accountMap.put(account1.getCoinId(),account1.getBalanceAmount());
                }
                for (MiningDetail detail : miningDetails1) {
                    miningMap.put(map2.get(detail.getAreaName()),detail.getTotalFee());
                }
                BigDecimal oldETH = accountMap.get(map2.get("ETH"));
                if (oldETH==null) oldETH =BigDecimal.ZERO;
                BigDecimal newETH = miningMap.get(map2.get("ETH"));
                if (newETH==null) newETH =BigDecimal.ZERO;
                BigDecimal oldUSDT = accountMap.get(map2.get("USDT"));
                if (oldUSDT==null) oldUSDT =BigDecimal.ZERO;
                BigDecimal newUSDT = miningMap.get(map2.get("USDT"));
                if (newUSDT==null) newUSDT =BigDecimal.ZERO;
                BigDecimal oldBTC = accountMap.get(map2.get("BTC"));
                if (oldBTC==null) oldBTC =BigDecimal.ZERO;
                BigDecimal newBTC = miningMap.get(map2.get("BTC"));
                if (newBTC==null) newBTC =BigDecimal.ZERO;
                BigDecimal ethBalance = oldETH.subtract(newETH);
                BigDecimal usdtBalance = oldUSDT.subtract(newUSDT);
                BigDecimal btcBalance = oldBTC.subtract(newBTC);
                BigDecimal ethBalanceUSDT =ethBalance.multiply(map1.get("ETH")).setScale(4,RoundingMode.HALF_UP);
                BigDecimal btcBalanceUSDT =btcBalance.multiply(map1.get("BTC")).setScale(4,RoundingMode.HALF_UP);
                BigDecimal totalUSDT=ethBalanceUSDT.add(btcBalanceUSDT).add(usdtBalance);
                Boolean  eth =ethBalance.compareTo(BigDecimal.ZERO)>0;
                Boolean  usdt =usdtBalance.compareTo(BigDecimal.ZERO)>0;
                Boolean  btc =btcBalance.compareTo(BigDecimal.ZERO)>0;
                if (eth){
                    if (usdt){
                        if (btc){
                            BigDecimal bigDecimal = miningMap.get(oldAccount.getCoinId());
                            if (bigDecimal==null) {
                                log.info("币种余额为0----"+oldAccount.getCoinId()+"--UserId--"+oldAccount.getUserId());
                                bigDecimal=BigDecimal.ZERO;
                            }
                            oldAccount.setBalanceAmount(oldAccount.getBalanceAmount().subtract(bigDecimal));
                            log.info("都满足一一扣减------"+oldAccount.getUserId());
                        }else {
                            BigDecimal add = ethBalanceUSDT.add(btcBalanceUSDT);
                            if (add.compareTo(BigDecimal.ZERO)>=0){
                                if (oldAccount.getCoinId().equals(map2.get("ETH"))){
                                    oldAccount.setBalanceAmount(add.divide(map1.get("ETH"),8,RoundingMode.HALF_UP));
                                    log.info("不满足btc扣减ETH------"+oldAccount.getUserId());
                                }
                            }else {
                                BigDecimal add1 = add.add(usdtBalance);
                                if (add1.compareTo(BigDecimal.ZERO)<0) {
                                    log.error("账户异常1---"+oldAccount.getUserId());
                                    continue;
                                }

                                if (oldAccount.getCoinId().equals(map2.get("USDT"))){
                                    oldAccount.setBalanceAmount(add1);
                                    log.info("不满足btc扣减USDT------"+oldAccount.getUserId());
                                }

                            }
                        }
                    }else {
                        if (btc){
                            BigDecimal add = ethBalanceUSDT.add(usdtBalance);
                            if (add.compareTo(BigDecimal.ZERO)>=0) {
                                if (oldAccount.getCoinId().equals(map2.get("ETH"))) {
                                    oldAccount.setBalanceAmount(add.divide(map1.get("ETH"), 8, RoundingMode.HALF_UP));
                                    log.info("不满足usdt扣减ETH------"+oldAccount.getUserId());
                                }
                            } else {
                                BigDecimal add1 = add.add(btcBalanceUSDT);
                                if (add1.compareTo(BigDecimal.ZERO)<0) {
                                    log.error("账户异常2---"+oldAccount.getUserId());
                                    continue;
                                }
                                if (oldAccount.getCoinId().equals(map2.get("BTC"))){
                                    oldAccount.setBalanceAmount(add1.divide(map1.get("BTC"),8,RoundingMode.HALF_UP));
                                    log.info("不满足usdt扣减BTC------"+oldAccount.getUserId());
                                }
                            }
                        }else {
                            if (totalUSDT.compareTo(BigDecimal.ZERO)<0) {
                                log.error("账户异常3---"+oldAccount.getUserId());
                                continue;
                            }
                            if (oldAccount.getCoinId().equals(map2.get("ETH"))) {
                                oldAccount.setBalanceAmount(totalUSDT.divide(map1.get("ETH"), 8, RoundingMode.HALF_UP));
                                log.info("只满足ETH------"+oldAccount.getUserId());
                            }
                        }
                    }
                }else {
                    if(usdt&&btc){
                        BigDecimal rate1= usdtBalance.divide(usdtBalance.add(btcBalanceUSDT),8,RoundingMode.HALF_UP);
                        BigDecimal rate2= btcBalanceUSDT.divide(usdtBalance.add(btcBalanceUSDT),8,RoundingMode.HALF_UP);
                        if (oldAccount.getCoinId().equals(map2.get("USDT"))) {
                            oldAccount.setBalanceAmount(totalUSDT.multiply(rate1));
                            log.info("rate1------"+oldAccount.getUserId());
                        }
                        if (oldAccount.getCoinId().equals(map2.get("BTC"))) {
                            oldAccount.setBalanceAmount(totalUSDT.multiply(rate2).divide(map1.get("BTC"),8,RoundingMode.HALF_UP));
                            log.info("rate2------"+oldAccount.getUserId());
                        }
                    }else {
                        if (usdt){
                            if (oldAccount.getCoinId().equals(map2.get("USDT"))){
                                oldAccount.setBalanceAmount(totalUSDT);
                                log.info("只满足USDT------"+oldAccount.getUserId());
                            }
                        }
                        if (btc){
                            if (oldAccount.getCoinId().equals(map2.get("BTC"))) {
                                oldAccount.setBalanceAmount(totalUSDT.divide(map1.get("BTC"),8,RoundingMode.HALF_UP));
                                log.info("只满足BTC------"+oldAccount.getUserId());
                            }
                        }
                        if(!usdt&&!btc){
                            log.error("账户异常4---"+oldAccount.getUserId());
                            continue;
                        }
                    }

                }

                if(oldAccount.getCoinId().compareTo(map2.get("HB"))==0){
                    BigDecimal  totalHB1 = miningDetail.getTotalFee()
                            .divide(HB2USDT,8,RoundingMode.HALF_UP)
                            .multiply(new BigDecimal(1.2)).setScale(8,RoundingMode.HALF_UP);
                    oldAccount.setBalanceAmount(oldAccount.getBalanceAmount().add(totalHB1));
//                  log.info("大于50%--HB----"+oldAccount.getUserId());
                }

            }
        }
        accountService.updateBatchById(oldAccounts);
    }

    private Map<Long,MiningDetail> getMiningDetail(HashMap<String,BigDecimal> map) {
        QueryWrapper<MiningDetail> ew = new QueryWrapper<>();
        ew.gt("total_fee", 0);
        List<MiningDetail> oldMiningDetails = miningDetailService.selectList(ew);
        Map<Long,MiningDetail> newDetailMap = new HashMap<>();
        for (MiningDetail oldMiningDetail : oldMiningDetails) {
            MiningDetail newMiningDetail = newDetailMap.get(oldMiningDetail.getUserId());
            BigDecimal bigDecimal = map.get(oldMiningDetail.getAreaName());
            BigDecimal totalFee = oldMiningDetail.getTotalFee().multiply(bigDecimal);
            if(newMiningDetail==null){
                newMiningDetail = new MiningDetail();
                newMiningDetail.setUserId(oldMiningDetail.getUserId());
                newMiningDetail.setTotalFee(totalFee);
                newDetailMap.put(oldMiningDetail.getUserId(),newMiningDetail);
            }else {
                newMiningDetail.setTotalFee(newMiningDetail.getTotalFee().add(totalFee));
            }
        }
        return newDetailMap;
    }

    private  Map<Long,Account> getAccount( Map<Long, BigDecimal> map) {
        QueryWrapper<Account> ew = new QueryWrapper<>();
        ew.gt("balance_amount", 0);
        List<Account> oldAccounts = accountService.selectList(ew);
        Map<Long,Account>  newAccounts = new HashMap<>();
        for (Account oldAccount : oldAccounts) {
            Account account = newAccounts.get(oldAccount.getUserId());
            BigDecimal bigDecimal = map.get(oldAccount.getCoinId());
            BigDecimal totalBalance = oldAccount.getBalanceAmount().multiply(bigDecimal);
            if(account==null){
                account = new Account();
                account.setUserId(oldAccount.getUserId());
                account.setBalanceAmount(totalBalance);
                newAccounts.put(oldAccount.getUserId(),account);
            }else {
                account.setBalanceAmount(account.getBalanceAmount().add(totalBalance));
            }
        }
        return newAccounts;
    }
}
