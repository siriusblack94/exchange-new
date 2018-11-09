package com.blockeng.admin.service.impl;


import com.blockeng.admin.dto.AccountBalanceStatiscDTO;
import com.blockeng.admin.entity.AccountBalance;
import com.blockeng.admin.entity.AccountFreeze;
import com.blockeng.admin.entity.Coin;
import com.blockeng.admin.mapper.*;
import com.blockeng.admin.service.AccountBalanceCountService;
import com.blockeng.framework.exception.AccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountBalanceCountServiceImpl implements AccountBalanceCountService {

    @Autowired
    CoinMapper coinMapper;

    @Autowired
    AccountBalanceCountMapper accountBalanceCountMapper;

    @Autowired
    EntrustOrderMapper entrustOrderMapper;

    @Autowired
    CoinWithdrawMapper coinWithdrawMapper;

    @Autowired
    CashWithdrawalsMapper cashWithdrawalsMapper;

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    CoinBuckleMapper coinBuckleMapper;

    @Override
    public AccountBalanceStatiscDTO accountBalance(String coinId,Integer accountType) {

        AccountBalanceStatiscDTO accountBalanceStatiscDTO=new AccountBalanceStatiscDTO();
        List<AccountBalance> listAccountBalance=new ArrayList<AccountBalance>();
        Coin coin=coinMapper.selectById(coinId);

        if(coin==null){
            throw new AccountException("币种异常");
        }

        String userCode="1";
        String robotCode="2";
        String userFlag="";
        String user="0";
        String robot="1,2";

        switch (accountType){
            case 0:
                break;
            case 1:
                userFlag=user;
                break;
            case 2:
                userFlag=robot;
                break;
            default:
                break;
        }
        //交易冻结资金
        List<AccountFreeze> resultListMap_transcationFreeze=entrustOrderMapper.selectTransactionFreezeByMarket(coin.getName(),userFlag);
        //提现冻结资金
        List<AccountFreeze> resultListMap_cashWithdrawFreeze=cashWithdrawalsMapper.selectCashRechargeFreeze(coinId,userFlag);
        //法币提现冻结资金
        List<AccountFreeze> resultListMap_coinWithdrawFreeze=coinWithdrawMapper.selectCoinWithdrawFreeze(coinId,userFlag);
        //补扣冻结
        List<AccountFreeze> resultListMap_buckleFreeze=coinBuckleMapper.selectBuckleFreeze(coinId,userFlag);
        //人数 可用余额
        List<Map<String,Object>> resultMapList=accountMapper.countUserNumberAndBalanceByFlag(coinId,userFlag);


        //都查出来之后 分类处理
        if(accountType==1||accountType==2){//只是一种用户
            //1种用户要考虑 真实用户包括0 2(用户机器人属于用户)
            AccountBalance accountBalance=new AccountBalance();
            resultMapList.forEach(resultMap->{

                resultMap.forEach((k,v)->{
                    if(k.equals("userCount")){
                        accountBalance.setUserCount(accountBalance.getUserCount()+((Long) v).intValue());
                    }
                    if(k.equals("available")){
                        accountBalance.setAvailableBalance(accountBalance.getAvailableBalance().add((BigDecimal)v));
                    }
                });
            });

            switch (accountType){
                case 1:
                    accountBalance.setUserType("真实用户");
                    break;
                case 2:
                    accountBalance.setUserType("机器人");
                    break;
                default:
                    break;
            }
            accountBalance.setWalletType(coin.getName());
            //总的提币冻结
            BigDecimal total_withdrawFreeze=new BigDecimal(0);
            BigDecimal total_transcationFreeze=new BigDecimal(0);
            BigDecimal total_buckleFreeze=new BigDecimal(0);

            //用户类型
            accountBalance.setUserFlag(accountType);
            //币种类型
            accountBalance.setWalletType(coin.getName());
            //提现冻结
            resultListMap_cashWithdrawFreeze.forEach(item->{
                total_withdrawFreeze.add(item.getFreeze());
            });

            resultListMap_coinWithdrawFreeze.forEach(item->{
                total_withdrawFreeze.add(item.getFreeze());
            });

            resultListMap_transcationFreeze.forEach(item->{
                total_transcationFreeze.add(item.getFreeze());
            });

            resultListMap_buckleFreeze.forEach(item->{
                total_buckleFreeze.add(item.getFreeze());
            });

            accountBalance.setWithdrawFreeze(total_withdrawFreeze);
            //交易冻结
            accountBalance.setTransactionFreeze(total_transcationFreeze);
            //总余额
            accountBalance.setTotalBalance(accountBalance.getAvailableBalance().add(accountBalance.getWithdrawFreeze()).add(accountBalance.getTransactionFreeze()));
            listAccountBalance.add(accountBalance);
            accountBalanceStatiscDTO.setWalletType(coin.getName());
            accountBalanceStatiscDTO.setWithdrawFreeze(accountBalance.getWithdrawFreeze());
            accountBalanceStatiscDTO.setAvailableBalance(accountBalance.getAvailableBalance());
            accountBalanceStatiscDTO.setTotalBalance(accountBalance.getTotalBalance());
            accountBalanceStatiscDTO.setTransactionFreeze(accountBalance.getTransactionFreeze());
            accountBalanceStatiscDTO.setBuckleFreeze(total_buckleFreeze);
            accountBalanceStatiscDTO.setRecords(listAccountBalance);
            return accountBalanceStatiscDTO;

        }else if(accountType==0){//两种类型账户都要统计

            AccountBalance accountBalance_user=new AccountBalance();
            AccountBalance accountBalance_robot=new AccountBalance();

            //用户类型
            accountBalance_user.setUserType(userCode);
            accountBalance_robot.setUserType(robotCode);

            //这个位置判断一下那个用户数据属于哪个结果集
            resultMapList.forEach(resultMap->{

                AccountBalance accountBalance_tmp=new AccountBalance();
//
                resultMap.forEach((k,v)->{

                    if(k.equals("userCount")){
                        accountBalance_tmp.setUserCount(accountBalance_tmp.getUserCount()+((Long) v).intValue());
                    }
                    if(k.equals("available")){
                        accountBalance_tmp.setAvailableBalance((BigDecimal)v);
                    }
                    if(k.equals("userFlag")){
                        accountBalance_tmp.setUserFlag((Integer) v);
                    }
                });

                //过滤非法规则用户
                if(accountBalance_tmp.getUserFlag()==null){
                    return;
                }
                //再定义一个临时变量 用于给两个对象赋值 不想写两遍一样的 用个引用先
                AccountBalance accountBalance_trans = null;

                if(user.contains(String.valueOf(accountBalance_tmp.getUserFlag()))){
                    accountBalance_trans=accountBalance_user;
                }else if(robot.contains(String.valueOf(accountBalance_tmp.getUserFlag()))){
                    accountBalance_trans=accountBalance_robot;
                }else {
                    return;
                }
                //累加
                accountBalance_trans.setUserCount(accountBalance_trans.getUserCount()+accountBalance_tmp.getUserCount());
                accountBalance_trans.setAvailableBalance(accountBalance_trans.getAvailableBalance().add(accountBalance_tmp.getAvailableBalance()));
            });

            //交易冻结
            resultListMap_transcationFreeze.forEach(item->{
                if(user.contains(String.valueOf(item.getUserFlag()))){
                    accountBalance_user.setTransactionFreeze(accountBalance_user.getTransactionFreeze().add(item.getFreeze()));
                }else if(robot.contains(String.valueOf(item.getUserFlag()))){
                    accountBalance_robot.setTransactionFreeze(accountBalance_robot.getTransactionFreeze().add(item.getFreeze()));
                }
            });

            //虚拟币提出冻结
            resultListMap_coinWithdrawFreeze.forEach(item->{
                if(user.contains(String.valueOf(item.getUserFlag()))){
                    accountBalance_user.setWithdrawFreeze(accountBalance_user.getWithdrawFreeze().add(item.getFreeze()));
                }else if(robot.contains(String.valueOf(item.getUserFlag()))){
                    accountBalance_robot.setWithdrawFreeze(accountBalance_robot.getWithdrawFreeze().add(item.getFreeze()));
                }
            });

            //法币提出冻结
            resultListMap_cashWithdrawFreeze.forEach(item->{
                if(user.contains(String.valueOf(item.getUserFlag()))){
                    accountBalance_user.setWithdrawFreeze(accountBalance_user.getWithdrawFreeze().add(item.getFreeze()));
                }else if(robot.contains(String.valueOf(item.getUserFlag()))){
                    accountBalance_robot.setWithdrawFreeze(accountBalance_robot.getWithdrawFreeze().add(item.getFreeze()));
                }
            });

            //补扣冻结
            resultListMap_buckleFreeze.forEach(item->{
                if(user.contains(String.valueOf(item.getUserFlag()))){
                    accountBalance_user.setBuckleFreeze(accountBalance_user.getBuckleFreeze().add(item.getFreeze()));
                }else if(robot.contains(String.valueOf(item.getUserFlag()))){
                    accountBalance_robot.setBuckleFreeze(accountBalance_robot.getBuckleFreeze().add(item.getFreeze()));
                }
            });

            //最后处理两个对象
            accountBalance_user.setTotalBalance(accountBalance_user.getAvailableBalance().add(accountBalance_user.getTransactionFreeze()).add(accountBalance_user.getWithdrawFreeze()).add(accountBalance_user.getBuckleFreeze()));
            accountBalance_user.setWalletType(coin.getName());
            accountBalance_user.setUserType("真实用户");
            accountBalance_user.setUserFlag(Integer.valueOf(userCode));

            accountBalance_robot.setTotalBalance(accountBalance_robot.getAvailableBalance().add(accountBalance_robot.getTransactionFreeze()).add(accountBalance_robot.getWithdrawFreeze()).add(accountBalance_robot.getBuckleFreeze()));
            accountBalance_robot.setWalletType(coin.getName());
            accountBalance_robot.setUserType("机器人账户");
            accountBalance_robot.setUserFlag(Integer.valueOf(robotCode));


            accountBalanceStatiscDTO.setWithdrawFreeze(accountBalance_user.getWithdrawFreeze().add(accountBalance_robot.getWithdrawFreeze()));
            accountBalanceStatiscDTO.setAvailableBalance(accountBalance_user.getAvailableBalance().add(accountBalance_robot.getAvailableBalance()));
            accountBalanceStatiscDTO.setTotalBalance(accountBalance_user.getTotalBalance().add(accountBalance_robot.getTotalBalance()));
            accountBalanceStatiscDTO.setTransactionFreeze(accountBalance_user.getTransactionFreeze().add(accountBalance_robot.getTransactionFreeze()));
            accountBalanceStatiscDTO.setBuckleFreeze(accountBalance_user.getBuckleFreeze().add(accountBalance_robot.getBuckleFreeze()));
            accountBalanceStatiscDTO.setWalletType(coin.getName());
            listAccountBalance.add(accountBalance_user);
            listAccountBalance.add(accountBalance_robot);

            accountBalanceStatiscDTO.setRecords(listAccountBalance);
            return accountBalanceStatiscDTO;
        }
        return accountBalanceStatiscDTO ;
    }

    @Override
    public List<Map<String, Object>> selectBalanceByUser(String userId) {
        Map<String,Object> paramMap=new HashMap<String,Object>();
        paramMap.put("userId",userId);
        return accountMapper.selectBalanceByUser(paramMap);
    }
}
