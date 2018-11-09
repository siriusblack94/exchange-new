package com.blockeng.mining.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blockeng.dto.ConfigDTO;
import com.blockeng.feign.ConfigServiceClient;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.security.UserDetails;
import com.blockeng.mining.dto.DividendRecordDetailDTO;
import com.blockeng.mining.entity.*;

import com.blockeng.mining.service.DividendAccountService;
import com.blockeng.mining.service.DividendRecordDetailService;
import com.blockeng.mining.service.DividendRecordService;
import com.blockeng.mining.utils.TimeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.math.BigDecimal;



/**
 * 邀请奖励
 * by crow
 * 2018年5月14日18:31:05
 */
@RestController
@RequestMapping("/dividend/account")
@Slf4j
@Api(value = "邀请奖励", description = "邀请奖励")
public class DividendAccountController {

    @Autowired
    private DividendAccountService dividendAccountService;
    @Autowired
    private ConfigServiceClient configServiceClient;

    @Autowired
    private DividendRecordService dividendRecordService;

    @Autowired
    private DividendRecordDetailService dividendRecordDetailService;
    /**
     * 查询挖矿信息
     *
     * @param page
     * @param userDetails
     * @return
     */
    @GetMapping("getList")
    @ApiOperation(value = "查询已经解冻的列表", notes = "查询已经解冻的列表", httpMethod = "GET"
            , authorizations = {@Authorization(value = "Authorization")})
    @PreAuthorize("isAuthenticated()")
    public Object dividendAccountUnlockList(
            @ApiIgnore Page<DividendReleaseRecord> page,
            @ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        return Response.ok(dividendAccountService.dividendAccountUnlockList(page, userDetails.getId()));
    }


    /**
     * 查询邀请记录列表
     *
     * @param page
     * @param userDetails
     * @return
     */
    @GetMapping("/getRecordList")
    @ApiOperation(value = "查询邀请记录列表", notes = "查询邀请记录列表", httpMethod = "GET"
            , authorizations = {@Authorization(value = "Authorization")})
    @PreAuthorize("isAuthenticated()")
    public Object dividendRecordList(
            @ApiIgnore Page<DividendRecord> page,
            @ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        QueryWrapper<DividendRecord> qw = new QueryWrapper<>();
        qw.eq("user_id", userDetails.getId());
        qw.orderByAsc("reward_date");
        return Response.ok(dividendRecordService.selectPage(page, qw));
    }
    /**
     * 查询邀请记录明细列表
     *
     * @param page
     * @param userDetails
     * @return
     */
    @GetMapping("/getDetailList/{rewardDate}")
    @ApiOperation(value = "查询邀请记录明细列表", notes = "查询邀请记录明细列表", httpMethod = "GET"
            , authorizations = {@Authorization(value = "Authorization")})
    @PreAuthorize("isAuthenticated()")
    public Object dividendRecordDetailList(
            @PathVariable("rewardDate") String rewardDate,
            @ApiIgnore Page<DividendRecordDetailDTO> page,
            @ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        QueryWrapper<DividendRecordDetailDTO> qw = new QueryWrapper<>();
        qw.eq("d.user_id", userDetails.getId());
       if (StringUtils.isNotBlank(rewardDate)){
           qw.eq("d.reward_date",rewardDate);
       }
       return Response.ok(dividendRecordDetailService.selectListPage(page, qw));
    }


    /**
     * 解冻本周奖励
     */
    /**
     * 查询本周可以获得挖矿信息
     *
     * @param userDetails
     * @return
     */
    @PostMapping("unlock")
    @ApiOperation(value = "解冻", notes = "解冻本周奖励", httpMethod = "POST"
            , authorizations = {@Authorization(value = "Authorization")})
    @PreAuthorize("isAuthenticated()")
    public Object unlockdividendAccount(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        boolean weekOneDay = TimeUtils.isWeekFirstDay(TimeUtils.getNowDay());

        ConfigDTO dayLimitConfig = configServiceClient.getConfig("Mining", "DayLimit");
        log.info("--解冻本周奖励dayLimitConfig--"+dayLimitConfig);
        Boolean dayLimit=true;
        if(dayLimitConfig!=null&&dayLimitConfig.getValue()!=null&&dayLimitConfig.getValue().toLowerCase().equals("off"))  dayLimit=false;

        if ((!weekOneDay)&&dayLimit) {//
            log.info(weekOneDay+"----是否周一");
            return Response.err(1000014, "解冻失败,每周第一天才能解冻");
        }
        ConfigDTO config = configServiceClient.getConfig("Mining", "ReleaseCountLimit");
        Boolean releaseCountLimit=true;
        if(config!=null&&config.getValue()!=null&&config.getValue().toLowerCase().equals("off"))  releaseCountLimit=false;
        log.info("--解冻本周奖励config--"+config);

        QueryWrapper<DividendAccount> qw = new QueryWrapper<>();
        qw.eq("user_id", userDetails.getId());
        DividendAccount dividendAccount = this.dividendAccountService.selectOne(qw);
        if ((null == dividendAccount||dividendAccount.getLockAmount().compareTo(BigDecimal.ZERO)<=0)) {//
            log.info("您目前没有邀请奖励可供释放");
            return Response.err(1000015, "您目前没有邀请奖励可供释放");
        }
        if (dividendAccount.getUnlockDate().equals(TimeUtils.getNowDay())&&releaseCountLimit) {//
            log.info("每周只能释放一次邀请奖励");
            return Response.err(1000015, "每周只能释放一次邀请奖励");
        }
        return dividendAccountService.unlockdividendAccount(userDetails.getId(), dividendAccount.getLockAmount());


    }


    /**
     * 累计推荐奖励分红
     *
     * @param userDetails
     * @return
     */
    @GetMapping("/total")
    @ApiOperation(value = "累计推荐奖励分红", notes = "累计推荐奖励分红", httpMethod = "GET"
            , authorizations = {@Authorization(value = "Authorization")})
    @PreAuthorize("isAuthenticated()")
    public Object dividendAccountTotal(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        return dividendAccountService.dividendAccountTotal(userDetails.getId());
    }



}
