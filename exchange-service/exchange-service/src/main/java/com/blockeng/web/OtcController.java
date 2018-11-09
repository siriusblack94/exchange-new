package com.blockeng.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blockeng.entity.Account;
import com.blockeng.entity.CashRecharge;
import com.blockeng.entity.CashWithdrawals;
import com.blockeng.entity.Config;
import com.blockeng.framework.constants.Constant;
import com.blockeng.framework.exception.GlobalDefaultException;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.security.UserDetails;
import com.blockeng.service.AccountService;
import com.blockeng.service.CashRechargeService;
import com.blockeng.service.CashWithdrawalsService;
import com.blockeng.service.ConfigService;
import com.blockeng.vo.CashRechargeForm;
import com.blockeng.vo.CashStatusForm;
import com.blockeng.vo.CashWithDrawalsForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 场外交易控制类
 * by crow
 * 2018年5月14日18:31:05
 */
@RestController
@RequestMapping("/otc")
@Slf4j
@Api(value = "数字货币-场外交易", description = "数字货币-场外交易")
public class OtcController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CashRechargeService cashRechargeService;

    @Autowired
    private CashWithdrawalsService cashWithdrawalsService;

    @Autowired
    private ConfigService configService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/account/{coinName}")
    @ApiOperation(value = "OUTSIDE-001 C2C账户资产", notes = "C2C账户资产", httpMethod = "GET",
            authorizations = {@Authorization(value = "Authorization")})
    public Object userAccount(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails, @PathVariable String coinName) {
        Account account = accountService.selectByUserAndCoinName(userDetails.getId(), coinName);
        if (!Optional.ofNullable(account).isPresent()) {
            //没有对应的C2C币种账户
            throw new GlobalDefaultException(50036);
        }
        QueryWrapper<Config> c = new QueryWrapper<>();
        c.eq("code", Constant.CONFIG_CNY2USDT);
        Config config = configService.selectOne(c);
        QueryWrapper<Config> c1 = new QueryWrapper<>();
        c1.eq("code", Constant.CONFIG_USDT2CNY);
        Config sellConfig = configService.selectOne(c1);
        Map<String, Object> result = new HashMap<>();
        result.put("balanceAmount", account.getBalanceAmount());
        result.put("freezeAmount", account.getFreezeAmount());
        result.put("amountUnit", coinName);
        result.put("coinId", account.getCoinId());
        result.put("buyRate", config.getValue());
        result.put("sellRate", sellConfig.getValue());
        return Response.ok(result);
    }

    /**
     * 法币充值（C2C买入）
     *
     * @param userDetails
     * @param cashRecharge
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/c2c/buy")
    @ApiOperation(value = "OUTSIDE-003 c2c买入", notes = "c2c买入", httpMethod = "POST",
            authorizations = {@Authorization(value = "Authorization")})
    public Object c2cBuy(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails,
                         @RequestBody @Valid CashRechargeForm cashRecharge) {
        return cashRechargeService.c2cBuy(cashRecharge, userDetails.getId());
    }

    /**
     * 法币提现（C2C卖出）
     *
     * @param userDetails
     * @param cashWithdrawals
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/c2c/sell")
    @ApiOperation(value = "OUTSIDE-004 c2c卖出", notes = "c2c卖出", httpMethod = "POST",
            authorizations = {@Authorization(value = "Authorization")})
    public Object c2cSell(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails,
                          @RequestBody @Valid CashWithDrawalsForm cashWithdrawals) {
        return cashWithdrawalsService.c2cSell(cashWithdrawals, userDetails);
    }

    /**
     * 法币充值记录
     *
     * @param form
     * @param userDetails
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/c2c/buy/record")
    @ApiOperation(value = "OUTSIDE-005 c2c买入记录", notes = "c2c买入记录", httpMethod = "POST")
    public Object c2cBuyList(@RequestBody CashStatusForm form, @ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        if (!Optional.ofNullable(userDetails).isPresent()) {
            throw new GlobalDefaultException(1000);
        }
        QueryWrapper<CashRecharge> e = new QueryWrapper<>();
        e.eq("user_id", userDetails.getId());

        if (null == form.getStatus()) {
            return Response.err(50032, "status不能为空");
        }
        if (form.getStatus() != 4) { //4的状态查询所有,所以不用
            e.eq("status", form.getStatus());
        }
        e.orderByDesc("created");
        IPage<CashRecharge> page = new Page<>(form.getCurrent(), form.getSize());
        return Response.ok(cashRechargeService.selectPage(page, e));
    }

    /**
     * 法币提现记录
     *
     * @param form
     * @param userDetails
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/c2c/sell/record")
    @ApiOperation(value = "OUTSIDE-005 c2c卖出记录", notes = "c2c卖出记录", httpMethod = "POST",
            authorizations = {@Authorization(value = "Authorization")})
    public Object c2cSellList(@RequestBody CashStatusForm form, @ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        QueryWrapper<CashWithdrawals> e = new QueryWrapper<>();
        e.eq("user_id", userDetails.getId());
//        if (null != form.getStatus()) {
//            e.eq("status", form.getStatus());
//        }

        if (form.getStatus() != 4) { //4的状态查询所有,所以不用
            e.eq("status", form.getStatus());
        }
        e.orderByDesc("created");
        IPage<CashWithdrawals> page = new Page<>(form.getCurrent(), form.getSize());
        return Response.ok(cashWithdrawalsService.selectPage(page, e));
    }
}
