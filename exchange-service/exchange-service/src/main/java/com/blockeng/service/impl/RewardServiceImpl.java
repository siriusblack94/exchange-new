package com.blockeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blockeng.entity.CashRecharge;
import com.blockeng.entity.RewardConfig;
import com.blockeng.framework.enums.BusinessType;
import com.blockeng.framework.enums.SwitchConfig;
import com.blockeng.service.AccountService;
import com.blockeng.service.CashRechargeService;
import com.blockeng.service.RewardConfigService;
import com.blockeng.service.RewardService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @Description: 奖励（注册奖励、邀请奖励）
 * @Author: shadow
 * @Date: Created in 2018/09/18
 */
@Service
@Slf4j
public class RewardServiceImpl implements RewardService {

    // 注册奖励配置
    private static final String REGISTER_REWARD = "register_reward";

    // 邀请奖励配置
    private static final String INVITE_REWARD = "invite_reward";

    //充值奖励
    private static final String RECHARGE_REWARD = "recharge_reward";

    @Autowired
    private AccountService accountService;

    @Autowired
    private RewardConfigService rewardConfigService;

    @Autowired
    private CashRechargeService cashRechargeService;

    /**
     * 注册奖励
     *
     * @param userId 用户ID
     * @return
     */
    @Override
    public boolean registerReward(Long userId) {
         return reward(userId,REGISTER_REWARD);

    }

    /**
     * 邀请奖励
     *
     * @param userId 用户ID
     * @return
     */
    @Override
    public boolean inviteReward(Long userId) {
        return reward(userId,INVITE_REWARD);
    }

    /**
     * 首次充值奖励
     *
     * @param userId 用户ID
     * @return
     */
    @Override
    public boolean rechargeReward(Long userId) {

        QueryWrapper<CashRecharge> qw = new QueryWrapper<>();

        qw.eq("user_id",userId);

        List<CashRecharge> cashRecharges = cashRechargeService.selectList(qw);

        if (cashRecharges.size()==1){
            return reward(userId,RECHARGE_REWARD);
        }
        return false;
    }

    /**
     * 奖励
     *
     * @param
     * @return
     */
    public Boolean reward(Long userId,String type){

        try {
            if (userId <= 0L) {
                log.error("用户ID错误");
                return false;
            }
            List<RewardConfig> rewardConfigs = rewardConfigService.queryByType(type);
            if (rewardConfigs==null || rewardConfigs.size()==0){
                log.error("未设置奖励");
                return false;
            }

            rewardConfigs.forEach(rewardConfig -> {

                if (!SwitchConfig.ON.getCode().equals(rewardConfig.getStatus())) {
                    log.error("币种ID："+rewardConfig.getCoinId()+"未开放奖励");
                    return;
                }
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String startTime = df.format(rewardConfig.getStartTime());
                String endTime =df.format(rewardConfig.getEndTime());
                DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                //时间解析
                DateTime start = DateTime.parse(startTime, format);
                //时间解析
                DateTime end = DateTime.parse(endTime, format);
                DateTime current = new DateTime();
                if (current.isBefore(start) || current.isAfter(end)) {
                    log.error("币种ID："+rewardConfig.getCoinId()+"不在奖励时间范围内");
                    return;
                }
                Long coinId = rewardConfig.getCoinId();
                BigDecimal amount = rewardConfig.getAmount();

                accountService.addAmountReward(userId, coinId, amount, BusinessType.getEnumByCode(type), BusinessType.getDescByCode(type));
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
