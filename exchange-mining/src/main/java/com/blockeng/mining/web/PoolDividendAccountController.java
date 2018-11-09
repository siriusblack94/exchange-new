package com.blockeng.mining.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blockeng.dto.ConfigDTO;
import com.blockeng.feign.ConfigServiceClient;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.security.UserDetails;
import com.blockeng.mining.entity.PoolDividendAccount;
import com.blockeng.mining.entity.PoolDividendRecord;
import com.blockeng.mining.service.PoolDividendAccountService;
import com.blockeng.mining.service.PoolDividendRecordService;
import com.blockeng.mining.utils.TimeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


/**
 * 邀请奖励
 * by crow
 * 2018年5月14日18:31:05
 */
@RestController
@RequestMapping("/pool/account")
@Slf4j
@Api(value = "矿池解冻详情", description = "矿池解冻详情")
public class PoolDividendAccountController {


    @Autowired
    private PoolDividendRecordService poolDividendRecordService;


    @Autowired
    private PoolDividendAccountService poolDividendAccountService;

    @Autowired
    private ConfigServiceClient configServiceClient;

    /**
     * 矿池解冻详情
     *
     * @param page
     * @param userDetails
     * @return
     */
    @GetMapping
    @ApiOperation(value = "矿池解冻列表", notes = "矿池解冻列表", httpMethod = "GET")
    @PreAuthorize("isAuthenticated()")
    public Object poolAccountList(
            @ApiIgnore Page<PoolDividendAccount> page,
            @ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        return poolDividendAccountService.poolAccountList(page, userDetails.getId());
    }


    /**
     * 解冻矿池奖励
     */
    /**
     * 持有平台币分红
     *
     * @param userDetails
     * @return
     */
    @PostMapping
    @ApiOperation(value = "解冻矿池奖励", notes = "解冻矿池奖励", httpMethod = "POST"
            , authorizations = {@Authorization(value = "Authorization")})
    @PreAuthorize("isAuthenticated()")
    public Object poolUnAccount(
            @ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        ConfigDTO dayLimitConfig = configServiceClient.getConfig("Mining", "DayLimit");
        log.info("--解冻矿池奖励dayLimitConfig--"+dayLimitConfig);
        Boolean dayLimit=true;
        if(dayLimitConfig!=null&&dayLimitConfig.getValue()!=null&&dayLimitConfig.getValue().toLowerCase().equals("off"))  dayLimit=false;
        ConfigDTO config = configServiceClient.getConfig("Mining", "ReleaseCountLimit");
        Boolean releaseCountLimit=true;
        if(config!=null&&config.getValue()!=null&&config.getValue().toLowerCase().equals("off"))  releaseCountLimit=false;
        log.info("--解冻矿池奖励config--"+config);
        boolean monethOneDay = TimeUtils.isMonethOneDay(TimeUtils.getNowDay());
        if (!monethOneDay&&dayLimit) {
            return Response.err(1000013, "解冻失败,每月第一天才能解冻");
        }

        QueryWrapper<PoolDividendAccount> qw = new QueryWrapper<>();
        qw.eq("user_id", userDetails.getId());
        qw.eq("unlock_date", TimeUtils.getNowDay());
        PoolDividendAccount poolDividendAccount = this.poolDividendAccountService.selectOne(qw);
        if (null != poolDividendAccount&&releaseCountLimit) {
            return Response.err(1000016, "解冻失败,每月只能解冻一次");
        }
        return poolDividendRecordService.poolUnAccount(userDetails.getId());
    }


    /**
     * 当前矿池的总的持币信息
     *
     * @param userDetails
     * @return
     */
    @GetMapping("/total")
    @ApiOperation(value = "当前矿池的总的持币信息", notes = "当前矿池的总的持币信息", httpMethod = "GET"
            , authorizations = {@Authorization(value = "Authorization")})
    @PreAuthorize("isAuthenticated()")
    public Object dividendAccountTotal(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        return Response.ok(poolDividendAccountService.dividendAccountTotal(userDetails.getId()));
    }


}
