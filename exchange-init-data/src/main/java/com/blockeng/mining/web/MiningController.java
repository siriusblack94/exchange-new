package com.blockeng.mining.web;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blockeng.framework.enums.BusinessType;
import com.blockeng.mining.entity.*;
import com.blockeng.mining.service.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class MiningController {

    @Autowired
    private UserService userService;

    @Autowired
    private TurnoverOrderService turnoverOrderService;
    @Autowired
    private MiningDetailService miningDetailService;
    @Autowired
    private MineService mineService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountDetailService accountDetailService;

    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }

    @RequestMapping("/do")
    public String doTime() {
        List<String> buyList = turnoverOrderService.getBuyUser();
        List<String> sellList = turnoverOrderService.getSellUser();
        Set<String> users = new HashSet<>();
        buyList.addAll(sellList);
        for(String userid:buyList){
            users.add(userid);
        }
        Iterator usersForEach = users.iterator();

        while (usersForEach.hasNext()){//遍历用户
            String userid = (String)usersForEach.next();
            Calendar cal = Calendar.getInstance();
            Date startDate = new Date(2018-1900,6,5,0,0,0);
            System.out.println(startDate);
            cal.setTime(startDate);
            Date endDate = new Date(2018-1900,7,16,23,59,59);
        while (cal.getTime().compareTo(endDate)<0) {
            java.text.Format format = new java.text.SimpleDateFormat("yyyy-MM-dd");
            List<TurnoverOrder> orderlist = turnoverOrderService.getOrderByUseridByDay(format.format(cal.getTime()) + " 00:00:01",
                    format.format(cal.getTime()) + " 23:59:59", userid);
            Map<String, BigDecimal> map = new HashMap<>();
            for (TurnoverOrder to : orderlist) {
                if (to.getSellUserId().equals(to.getBuyUserId())) {
                    BigDecimal pr = to.getAmount().multiply(to.getSellFeeRate()).multiply(new BigDecimal("2"));
                    System.out.print("machine:" + to.getMarketName());
                    String areaName = to.getMarketName().substring(to.getMarketName().indexOf("/") + 1);
                    if (map.get(areaName) != null) {
                        map.put(areaName, map.get(areaName).add(pr));
                    } else {
                        map.put(areaName, pr);
                    }
                } else {
                    BigDecimal pr = to.getAmount().multiply(to.getSellFeeRate());
                    System.out.print("person :" + to.getMarketName());
                    String areaName = to.getMarketName().substring(to.getMarketName().indexOf("/") + 1);
                    if (map.get(areaName) != null) {
                        map.put(areaName, map.get(areaName).add(pr));
                    } else {
                        map.put(areaName, pr);
                    }
                }
            }
            if (map.size() > 0) {
                Iterator iterator = map.keySet().iterator();
                while (iterator.hasNext()) {
                    String areaName = (String)iterator.next();
                    BigDecimal pr = map.get(areaName);
                    System.out.println(areaName);
                    String insertSql = "insert into ex_trade.mining_detail(user_id,area_name,total_fee,time_mining,last_update_time,created) " +
                            "  values('" + userid + "','" + areaName + "'," + pr + ",'" + format.format(cal.getTime()) + "',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP) ;";
                    log.info(insertSql);
                }
            }
            cal.add(Calendar.DAY_OF_MONTH, 1);// 日期减1
        }
     }
        return "ok";

    }



    @RequestMapping("/mine")
    public String mine() {
        QueryWrapper<MiningDetail> ew = new QueryWrapper<>();
        List<MiningDetail> miningDetails = miningDetailService.selectList(ew);
        if (CollectionUtils.isEmpty(miningDetails)) {
            log.error("当前不存在挖矿数据");
            return null;
        }
        Map<Long, List<MiningDetail>> userMiningMap = miningDetails.stream().collect(Collectors.groupingBy(MiningDetail::getUserId));

        Map<String, BigDecimal> coins = new HashMap<>();
        coins.put("USDT", new BigDecimal(6.85));
        coins.put("ETH", new BigDecimal(2047));
        coins.put("BTC", new BigDecimal(43867));
        List<Mine> mineList = new ArrayList<>(userMiningMap.size());//初始化

        for (Map.Entry<Long, List<MiningDetail>> entry : userMiningMap.entrySet()) {
            Long userId = entry.getKey();
            List<MiningDetail> miningList = entry.getValue();
            BigDecimal realTotalBxxTotalVolume = BigDecimal.ZERO;
            for (MiningDetail item : miningList) { //计算单个用户挖矿总额度
                String areaName = item.getAreaName().toUpperCase();
                BigDecimal bxxPrice = coins.get(areaName);
                realTotalBxxTotalVolume = realTotalBxxTotalVolume.add(item.getTotalFee().multiply(bxxPrice)).setScale(4,BigDecimal.ROUND_HALF_UP);
            }
            mineList.add(new Mine().setUserId(userId).setTotalMining(realTotalBxxTotalVolume).setRealMining(realTotalBxxTotalVolume));
            String sql1 = "insert into ex_trade.mine(user_id,total_mining,real_mining) values ("+userId+","+realTotalBxxTotalVolume+","+realTotalBxxTotalVolume+");";
            log.info(sql1);
        }
        mineService.insertBatch(mineList);//插入挖矿数据总和
        Long mineCoinId = 1016312602449911810L;
        for (Mine mine : mineList) {//遍历当天挖矿所有数据

            QueryWrapper<Account> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", mine.getUserId())
                    .eq("coin_id", mineCoinId);
            wrapper.last("LIMIT 1");
            Account account = accountService.selectOne(wrapper);

            if (accountService.addAmount(account.getId(), mine.getRealMining()) > 0) {
                // 保存流水
               String sql =  "UPDATE ex_trade.account SET balance_amount = balance_amount + "+mine.getRealMining()+" WHERE id ="+account.getId()+";";
               log.info(sql);
                AccountDetail accountDetail = new AccountDetail(mine.getUserId(),
                        mineCoinId,
                        account.getId(),
                        account.getId(),
                        mine.getId(),
                        1,
                        BusinessType.MINE_DIG.getCode(),
                        mine.getRealMining(),
                        BusinessType.MINE_DIG.getDesc());
                accountDetailService.insert(accountDetail);
                String sql2 = "insert into ex_trade.account_detail(user_id,coin_id,account_id,ref_account_id,direction,business_type,amount,remark) " +
                        "values ("+mine.getUserId()+","+mineCoinId+","+ account.getId()+","+account.getId()+",1,'mine_dig',"+mine.getRealMining()+",'挖矿分红');";
                log.info(sql2);
            }
        }
        return "ok";
    }


    @RequestMapping("/changePassword")
    public String changePassword() {
//        log.info("changePassword-----start----");
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("flag",0);
        List<User> users = userService.selectList(wrapper);
        for (int i = 0; i <=users.size()/2000+1; i++) {
            int finalI = i;
            new Thread(() -> {
                for(int j=0 ;j<2000;j++){
                    int k = j+ finalI *2000;
                    if(k>users.size()) break;
                    User user = users.get(k);
                    String password = user.getPassword();
                    String paypassword =user.getPaypassword() ;
                    String new_password = password!=null?new BCryptPasswordEncoder().encode(password.toLowerCase()):"";
                    String new_payPassword = paypassword!=null?new BCryptPasswordEncoder().encode(paypassword.toLowerCase()):"";
                    user.setPassword(new_password);user.setPaypassword(new_payPassword);
                    log.info("update 'user' set password = '"+new_password+"',paypassword = '"+new_payPassword+"' where id = '"+user.getId()+"';");
                }
            }).start();
        }
//        for (User user : users) {
//            String password = user.getPassword();
//            String paypassword =user.getPaypassword() ;
//            String new_password = password!=null?new BCryptPasswordEncoder().encode(password.toLowerCase()):"";
//            String new_payPassword = paypassword!=null?new BCryptPasswordEncoder().encode(paypassword.toLowerCase()):"";
//            user.setPassword(new_password);user.setPaypassword(new_payPassword);
//            log.info("update 'user' set password = "+new_password+",paypassword = "+new_payPassword+" where id = "+user.getId());
//        }
       // userService.updateBatchById(users);
        return "ok";
    }




}
