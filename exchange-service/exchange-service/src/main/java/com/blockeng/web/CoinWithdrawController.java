package com.blockeng.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blockeng.entity.CoinWithdraw;
import com.blockeng.framework.dto.ApplyWithdrawDTO;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.security.UserDetails;
import com.blockeng.service.AccountService;
import com.blockeng.service.CoinWithdrawService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @Description: 数字货币提现 Controller
 * @Author: Chen Long
 * @Date: Created in 2018/5/22 下午5:21
 * @Modified by: Chen Long
 */
@RestController
@RequestMapping("/withdraw")
@Slf4j
@Api(value = "资产管理-数字货币提现", description = "资产管理-数字货币提现")
public class CoinWithdrawController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CoinWithdrawService coinWithdrawService;

    /**
     * 申请提币
     *
     * @return
     */
    @PostMapping
    @ApiOperation(value = "COIN-WITHDRAW-001 申请提币", notes = "申请提币", httpMethod = "POST"
            , authorizations = {@Authorization(value = "Authorization")})
    @ApiImplicitParam(name = "applyWithdraw", value = "申请提币参数", dataType = "ApplyWithdrawDTO", paramType = "body")
    @PreAuthorize("isAuthenticated()")
    public Object applyWithdraw(@RequestBody ApplyWithdrawDTO applyWithdraw,
                                @ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        return accountService.applyWithdraw(applyWithdraw, userDetails.getId());
    }

    /**
     * 查询用户提现记录
     *
     * @param userDetails
     * @param page
     * @param coinId
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/record")
    @ApiOperation(value = "用户提现", httpMethod = "GET", authorizations = {@Authorization(value = "Authorization")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页码", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页大小", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "coinId", value = "币种ID", dataType = "long", paramType = "query")
    })
    public Object queryWithdrawRecord(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails,
                                      @ApiIgnore Page<CoinWithdraw> page,
                                      @RequestParam("coinId") long coinId) {
        QueryWrapper<CoinWithdraw> e = new QueryWrapper<>();
        e.eq("user_id", userDetails.getId());
        if (coinId != 0L) {
            e.eq("coin_id", coinId);
        }
        e.orderByDesc("created");
        IPage<CoinWithdraw> coinWithdrawPage = coinWithdrawService.selectPage(page, e);
        return Response.ok(coinWithdrawPage);
    }
}
