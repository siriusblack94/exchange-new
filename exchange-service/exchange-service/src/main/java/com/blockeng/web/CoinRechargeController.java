package com.blockeng.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blockeng.entity.CoinRecharge;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.security.UserDetails;
import com.blockeng.service.AccountService;
import com.blockeng.service.CoinRechargeService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @Description: 数字货币充值 Controller
 * @Author: Chen Long
 * @Date: Created in 2018/5/22 下午5:21
 * @Modified by: Chen Long
 */
@RestController
@RequestMapping("/recharge")
@Slf4j
@Api(value = "资产管理-充值", description = "资产管理-充值")
public class CoinRechargeController {

    @Autowired
    private CoinRechargeService coinRechargeService;

    @Autowired
    private AccountService accountService;

    /**
     * 获取充值地址
     */
    @GetMapping("/address/{coinId}")
    @ApiOperation(value = "COIN-RECHARGE-001 获取充值地址(过时,请使用币种名称)", notes = "获取充值地址", httpMethod = "GET"
            , authorizations = {@Authorization(value = "Authorization")})
    @ApiImplicitParam(name = "coinId", value = "币种ID", dataType = "String", paramType = "path")
    @PreAuthorize("isAuthenticated()")
    public Object rechargeAddress(@PathVariable("coinId") Long coinId,
                                  @ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        return Response.ok(accountService.queryRechargeAddress(coinId, userDetails.getId()));
    }


//    /**
//     * 获取充值地址
//     */
//    @GetMapping("/address/{coinName}")
//    @ApiOperation(value = "COIN-RECHARGE-001 获取充值地址", notes = "获取充值地址", httpMethod = "GET"
//            , authorizations = {@Authorization(value = "Authorization")})
//    @ApiImplicitParam(name = "coinName", value = "币种名称", dataType = "String", paramType = "path")
//    @PreAuthorize("isAuthenticated()")
//    public Object rechargeAddress(@PathVariable("coinName") String coinName,
//                                  @ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
//        return Response.ok(accountService.queryRechargeAddress(coinName, userDetails.getId()));
//    }

    /**
     * 获取充值记录
     *
     * @param coinId      币种ID
     * @param page        分页参数
     * @param userDetails 登录用户
     * @return
     */
    @GetMapping("/record")
    @ApiOperation(value = "COIN-RECHARGE-002 获取充值记录", notes = "获取充值记录", httpMethod = "GET", authorizations = {@Authorization(value = "Authorization")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页码", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页大小", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "coinId", value = "币种ID", dataType = "long", paramType = "query")
    })
    @PreAuthorize("isAuthenticated()")
    public Object rechargeRecord(@RequestParam("coinId") long coinId,
                                 @ApiIgnore Page<CoinRecharge> page,
                                 @ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        QueryWrapper<CoinRecharge> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userDetails.getId());
        if (coinId != 0L) {
            wrapper.eq("coin_id", coinId);
        }
        wrapper.orderByDesc("created");
        IPage<CoinRecharge> coinRechargePage = coinRechargeService.selectPage(page, wrapper);
        return Response.ok(coinRechargePage);
    }
}
