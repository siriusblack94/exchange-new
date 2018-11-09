package com.blockeng.web;

import com.blockeng.dto.SymbolAssetDTO;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.security.UserDetails;
import com.blockeng.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @Description: 币币交易用户资产 Controller
 * @Author: Chen Long
 * @Date: Created in 2018/5/15 上午11:46
 * @Modified by: Chen Long
 */
@RestController
@RequestMapping("/user/account")
@Slf4j
@Api(value = "币币交易用户资产", description = "币币交易用户资产 REST API")
public class UserAccountController {

    @Autowired
    private AccountService accountService;

    /**
     * 币币交易用户交易对账户资产
     *
     * @param symbol 交易对标识符
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/asset/{symbol}")
    @ApiOperation(value = "ACCOUNT-001 币币交易用户交易对账户资产", notes = "币币交易用户交易对账户资产", httpMethod = "GET"
            , authorizations = {@Authorization(value = "Authorization")})
    @ApiImplicitParam(name = "symbol", value = "交易对标识符", required = true, dataType = "String", paramType = "path")
    @ResponseBody
    public Object asset(@PathVariable("symbol") String symbol,
                        @ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        SymbolAssetDTO symbolAsset = accountService.queryAccount(symbol, userDetails.getId());
        return Response.ok(symbolAsset);
    }
}

