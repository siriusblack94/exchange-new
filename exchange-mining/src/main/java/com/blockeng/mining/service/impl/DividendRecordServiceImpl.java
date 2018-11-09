package com.blockeng.mining.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.dto.TradeMarketDTO;
import com.blockeng.feign.ConfigServiceClient;
import com.blockeng.mining.dto.DividendRecordDTO;
import com.blockeng.mining.dto.NowWeekDividendDTO;
import com.blockeng.mining.entity.DividendRecord;
import com.blockeng.mining.entity.DividendRecordDetail;
import com.blockeng.mining.entity.Mine;
import com.blockeng.mining.entity.User;
import com.blockeng.mining.mapper.DividendAccountMapper;
import com.blockeng.mining.mapper.DividendRecordDetailMapper;
import com.blockeng.mining.mapper.DividendRecordMapper;
import com.blockeng.mining.service.*;
import com.blockeng.mining.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@Slf4j
@Transactional
public class DividendRecordServiceImpl extends ServiceImpl<DividendRecordMapper, DividendRecord> implements DividendRecordService {

    @Autowired
    private MineHelpService mineHelpService;

    @Autowired
    private MineService mineService;

    @Autowired
    private ConfigServiceClient configServiceClient;

    @Autowired
    private UserService userService;
    @Autowired
    private DividendAccountMapper dividendAccountMapper;

    @Autowired
    private DividendRecordDetailMapper dividendRecordDetailMapper;

    @Override
    public Integer saveRecord(BigDecimal dayTotalMining, String rate, Long refeId, Long userId, int enable, String mark) {
       return this.baseMapper.insert(new DividendRecord().
                setRewardDate(TimeUtils.getYesterdayDate()).
                setEnable(enable).
                setInviteAmount(dayTotalMining).
                setScaleAmount(rate).
                setMark(mark).
                setRefeUserId(refeId).
                setUserId(userId));
    }


    @Override
    public NowWeekDividendDTO dividendAccountThisWeek(Long userId) {
        QueryWrapper<DividendRecord> ew = new QueryWrapper<>();
        ew.eq("user_id", userId);
        ew.between("reward_date", TimeUtils.getWeekFirst(null), TimeUtils.getWeekLast(null));
        BigDecimal totalAmount = this.baseMapper.selectTotalThisWeek(ew);
        if (null == totalAmount || totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return new NowWeekDividendDTO();
        }
        TradeMarketDTO mineCurrentMarket = mineHelpService.getMineCurrentMarket();
        return new NowWeekDividendDTO().setTotalAmount(totalAmount).
                setUsdtAmount(totalAmount.multiply(mineCurrentMarket.getPrice())).
                setCnyAmount(totalAmount.multiply(mineCurrentMarket.getCnyPrice()));
    }


    @Override
    public BigDecimal selectTotalPriWeek(Long userId) {
        QueryWrapper<DividendRecord> ew = new QueryWrapper<>();
        ew.eq("user_id", userId);
        ew.between("reward_date", TimeUtils.getWeekPriFirst(null), TimeUtils.getWeekPriLast(null));
        return this.baseMapper.selectTotalThisWeek(ew);
    }


    @Override
    public Object dividendAccountPriWeek(String unLockDate, Long id) {
        String priFirst = TimeUtils.getWeekPriFirst(unLockDate);
        String priLast = TimeUtils.getWeekPriLast(unLockDate);
        QueryWrapper<DividendRecord> ew = new QueryWrapper<>();
        ew.eq("user_id", id);
        ew.between("reward_date", priFirst, priLast);
        List<DividendRecord> dividendRecords = super.baseMapper.selectList(ew);
        List<DividendRecordDTO> dividendRecordDTOList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(dividendRecords)) {
            TradeMarketDTO mineCurrentMarket = mineHelpService.getMineCurrentMarket();
            for (DividendRecord item : dividendRecords) {
                DividendRecordDTO dividendRecordDTO = new DividendRecordDTO();
                dividendRecordDTO.setRewardDate(item.getRewardDate())
                        .setInviteAmount(item.getInviteAmount())
                        .setCnyAccount(item.getInviteAmount().multiply(mineCurrentMarket.getCnyPrice()))
                        .setUsdtAccount(item.getInviteAmount().multiply(mineCurrentMarket.getPrice()));
                dividendRecordDTOList.add(dividendRecordDTO);
            }
        }
        return dividendRecordDTOList;
    }


    /**
     * 用户邀请关系奖励
     */
    @Override
    public void inviteRelation()  {

        String yesterdayDate = TimeUtils.getYesterdayDate() ;


        if (null != this.baseMapper.selectOne(new QueryWrapper<DividendRecord>().eq("reward_date", yesterdayDate).last("limit 1"))) {//挖矿数据已经统计
            log.info("当天邀请数据已经清算");
            return;
        }
        String limitRate = configServiceClient.getConfig("Mining", "INVITE_LIMIT").getValue();//邀请奖励释放上限
        String amount = configServiceClient.getConfig("Mining", "AMOUNT").getValue();//平台币总量

        BigDecimal inviteAmount = this.baseMapper.selectAllDivide();
        BigDecimal limitAmount = new BigDecimal(amount).multiply(new BigDecimal(limitRate));
        //判断邀请总释放量
        if(inviteAmount.compareTo(limitAmount)>=0){
            log.error("超额了，不送了");
        }else{
            String enoughRate = configServiceClient.getConfig("Mining", "ENOUGH_RATE").getValue();//超级矿工返佣比例
            String secondRate = configServiceClient.getConfig("Mining", "SECOND_RATE").getValue();//二级返佣
            String notEnoughRate = configServiceClient.getConfig("Mining", "NOT_ENOUGH_RATE").getValue();//一般返佣规则
            Long inviteNum = Long.valueOf(configServiceClient.getConfig("Mining", "INVITE_NUM").getValue());//超级矿工邀请人数条件

            if (0 == inviteNum || null == notEnoughRate | null == enoughRate) {
                log.info("参数配置错误:inviteNum" + inviteNum + " notEnoughRate:" + notEnoughRate + "  enoughRate:" + enoughRate);
                return;
            }
            /******************************* 将实名认证放在Map里，下面好用   start***********************/
            List<User> authStatusUserList = this.userService.authStatusList(); //所有已经实名认证的用户
            if (CollectionUtils.isEmpty(authStatusUserList)) {
                log.info("当前系统用户都未实名认证");
                return;
            }

            Map<Long, User> authStatusUserMap = new HashMap<>();
            for (User item : authStatusUserList) {
                authStatusUserMap.put(item.getId(), item);
            }
            authStatusUserList.clear();
//            log.info("实名认证用户数量:------"+authStatusUserMap.size());
            /******************************* 将实名认证放在Map里，下面好用   end******/

            /*************************************    分组用户  start   *********************/
            List<User> inviteUserList = userService.inviteList(); //存在邀请关系的所有用户
//            log.info("存在邀请关系:------"+inviteUserList.size());
            if (CollectionUtils.isEmpty(inviteUserList)) {
                log.info("不存在邀请用户数据");
                return;
            }
            //分组用户  start
            Set<Long> userSet = new HashSet<>();
            for(User user:inviteUserList){
                userSet.add(user.getId());
            }
//            log.info("分组前用户数量:------"+userSet.size());
            Map<Long, List<User>> inviteUserMap = new HashMap<>();
            Iterator<Long> userIterator = userSet.iterator();
            while (userIterator.hasNext()){
                Long userId = userIterator.next();
                List<User> userList = new ArrayList<>();
                for(User user:inviteUserList){
                    if(user.getDirectInviteid().compareTo(userId)==0 ){  //被邀请人等于当前循环的用户
                        userList.add(user);
                    }
                }
                if(userList.size()>0){
                    inviteUserMap.put(userId,userList);
                }
            }
//            log.info("邀请人数量:------"+inviteUserMap.size());
            /*************************************    分组用户  end   ********/


            /*************************************    当天用户挖矿信息  start   *********************/
            QueryWrapper<Mine> ew = new QueryWrapper<>();
            ew.eq("time_mining", yesterdayDate);
            List<Mine> mines = mineService.selectList(ew);//当天参入挖矿的所有用户
            if (CollectionUtils.isEmpty(mines)) {
                log.info("不存在任何挖矿数据");
                return;
            }
            Map<Long, Mine> mineMap = new HashMap<>();
            for (Mine item : mines) { //按照userid的形式,存储用户挖矿信息,便于查询
                mineMap.put(item.getUserId(), item);
            }
//            log.info("当天挖矿人数量:------"+mineMap.size());
            /*************************************    当天用户挖矿信息    end   *****/


            for (Map.Entry<Long, List<User>> entry : inviteUserMap.entrySet()) {//遍历计算邀请用户的挖矿总的额度
                List<User> users = entry.getValue();
                BigDecimal dayTotalMining = BigDecimal.ZERO;
//                log.info("遍历的人:------"+entry.getKey());
//                log.info("下级:------"+users.size());
                for (User item : users) {//遍历计算
                    Mine mine = mineMap.get(item.getId());
                    if (null != mine) { //如果mine不为空说明当天参入了挖矿
                        DividendRecordDetail dividendRecordDetail = new DividendRecordDetail();
                        dividendRecordDetail
                                .setUserId(entry.getKey())
                                .setRefeUserId(mine.getUserId())
                                .setInviteAmount(mine.getRealMining())
                                .setRewardDate(yesterdayDate)
                                .setEnable(1)
                                .setCreated(new Date());
                        dividendRecordDetailMapper.insert(dividendRecordDetail);
                        dayTotalMining = dayTotalMining.add(mine.getRealMining());
                    }
                }
//                log.info("下级挖矿量:------"+dayTotalMining);
                if (dayTotalMining.compareTo(BigDecimal.ZERO) > 0) {  //如果挖矿总额度大于0
                    if (users.size() >= inviteNum) {//如果邀请大于20,直接奖励给当前用户
                        dayTotalMining = dayTotalMining.multiply(new BigDecimal(enoughRate)).setScale(4, RoundingMode.DOWN);//邀请奖励额度
                        inviteAmount = this.baseMapper.selectAllDivide();
                        //超额判断
                        if((dayTotalMining.add(inviteAmount)).compareTo(limitAmount)>=0){
                            log.error("超额了，不送了");
                            break;
                        }
                        if(saveRecord(dayTotalMining, enoughRate, entry.getKey(), Long.valueOf(entry.getKey()), 1, "success")>0)
                      dividendAccountMapper.insertOrUpdate(entry.getKey(),dayTotalMining,new Date(),yesterdayDate);
                    } else { //只能给百分之十,吧另外的金额返回给上级满足条件的,或者平台
                        dayTotalMining = dayTotalMining.multiply(new BigDecimal(notEnoughRate)).setScale(4, RoundingMode.DOWN);//邀请奖励额度
                        inviteAmount = this.baseMapper.selectAllDivide();
                        //超额判断
                        if((dayTotalMining.add(inviteAmount)).compareTo(limitAmount)>=0){
                            log.error("超额了，不送了");
                            break;
                        }
                        User user = authStatusUserMap.get(entry.getKey());
                        if (saveRecord(dayTotalMining, notEnoughRate, entry.getKey(), Long.valueOf(entry.getKey()), 1, "success")>0)
                        dividendAccountMapper.insertOrUpdate(entry.getKey(),dayTotalMining,new Date(),yesterdayDate);
                        boolean breakStatus = false;
                        while (null != user) {
                            Long superDirectInviteid = user.getDirectInviteid();//获取他上级的userId
                            if ( superDirectInviteid <= 0) {//没有找到上级
                                saveRecord(dayTotalMining, secondRate, Long.valueOf(user.getDirectInviteid()), -1L, 0, "当前用户无上级,直接分配给平台");
                                break;
                            }
                            user = authStatusUserMap.get(superDirectInviteid);
                            if (null == user) {//没有实名认证
                                saveRecord(dayTotalMining, secondRate, superDirectInviteid, -1L, 0, "当前账户未实名认证");
                                break;
                            }
                            List<User> list = inviteUserMap.get(user.getId());
                            if (list.size() >= inviteNum) {//找到了,就给这个人就好了
                                //二级返佣e
                                dayTotalMining = dayTotalMining.multiply(new BigDecimal(secondRate)).setScale(4, RoundingMode.DOWN);//邀请奖励额度
                                inviteAmount = this.baseMapper.selectAllDivide();
                                //超额判断
                                if((dayTotalMining.add(inviteAmount)).compareTo(limitAmount)>=0){
                                    log.error("超额了，不送了");
                                    breakStatus = true;
                                    break;
                                }
//                                log.info("二级奖励userId" + user.getId());
                                if (saveRecord(dayTotalMining, secondRate, Long.valueOf(entry.getKey()), user.getId(), 1, "success")>0)
                                dividendAccountMapper.insertOrUpdate( user.getId(),dayTotalMining,new Date(),yesterdayDate);
                                break;
                            }
                        }
                        if(breakStatus){
                            log.error("二级break,超额了，不送了");
                            break;
                        }
                    }
                }
            }


        }

    }


}
