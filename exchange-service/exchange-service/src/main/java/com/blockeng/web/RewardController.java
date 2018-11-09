package com.blockeng.web;

import com.blockeng.entity.Config;
import com.blockeng.entity.InviteRewardsAccount;
import com.blockeng.framework.enums.MiningConfig;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.security.UserDetails;
import com.blockeng.service.AccountService;
import com.blockeng.service.ConfigService;
import com.blockeng.service.RewardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Optional;

/**
 * @Description: 奖励（注册奖励、邀请奖励）
 * @Author: Chen Long
 * @Date: Created in 2018/5/25 下午4:52
 * @Modified by: Chen Long
 */
@RestController
@RequestMapping("/reward")
@Api(value = "邀请奖励", description = "邀请奖励", tags = "邀请奖励")
public class RewardController {

    @Autowired
    private RewardService rewardService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ConfigService configService;

    /**
     * 注册奖励
     *
     * @param userId 用户ID
     * @return
     */
    @PostMapping("/register/{userId}")
    public boolean registerReward(@PathVariable("userId") Long userId) {
        return rewardService.registerReward(userId);
    }

    /**
     * 邀请奖励
     *
     * @param userId 用户ID
     * @return
     */
    @PostMapping("/invite/{userId}")
    public boolean inviteReward(@PathVariable("userId") Long userId) {
        return rewardService.inviteReward(userId);
    }

    /**
     * 首次充值奖励
     *
     * @param userId 用户ID
     * @return
     */
    @PostMapping("/recharge/{userId}")
    public boolean rechargeReward(@PathVariable("userId") Long userId) {
        return rewardService.rechargeReward(userId);
    }



    @ApiOperation(value = "可解冻邀请奖励", notes = "可解冻邀请奖励", httpMethod = "GET",
            authorizations = {@Authorization(value = "Authorization")})
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/info")
    Object info(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        Config config = configService.queryByTypeAndCode(MiningConfig.TYPE.getValue(), MiningConfig.CODE_COIN_ID.getValue());
        Long coinId = Long.parseLong(config.getValue());

        InviteRewardsAccount inviteRewardsAccount = mongoTemplate.findOne(new Query(Criteria.where("user_id").is(userDetails.getId())), InviteRewardsAccount.class);
        if (!Optional.ofNullable(inviteRewardsAccount).isPresent()) {
            inviteRewardsAccount = new InviteRewardsAccount();
        }
        inviteRewardsAccount.setCoinId(coinId);
        return Response.ok(inviteRewardsAccount);
    }

    @ApiOperation(value = "解冻邀请奖励", notes = "解冻邀请奖励", httpMethod = "POST",
            authorizations = {@Authorization(value = "Authorization")})
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/unfreeze")
    Object unfreeze(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        return Response.ok(accountService.unfreezeInviteRewards(userDetails.getId()));
    }
}
