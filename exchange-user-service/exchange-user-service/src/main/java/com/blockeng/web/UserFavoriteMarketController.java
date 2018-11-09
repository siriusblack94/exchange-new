package com.blockeng.web;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blockeng.entity.UserFavoriteMarket;
import com.blockeng.feign.MarketServiceClient;
import com.blockeng.framework.exception.GlobalDefaultException;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.security.UserDetails;
import com.blockeng.service.UserFavoriteMarketService;
import com.blockeng.web.vo.UserFavoriteMarketForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Optional;

/**
 * @author qiang
 */
@RestController
@RequestMapping("/favorite")
@Api(value = "用户收藏交易市场", tags = "用户收藏交易市场")
public class UserFavoriteMarketController {


    @Autowired
    private MarketServiceClient marketServiceClient;

    @Autowired
    private UserFavoriteMarketService userFavoriteMarketService;

    @PostMapping("/addFavorite")
    @ApiOperation(value = "添加用户交易市场", notes = "添加用户交易市场", httpMethod = "POST", authorizations = {@Authorization(value = "Authorization")})
    public Object addFavorite(@RequestBody UserFavoriteMarketForm form, @ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        if (!Optional.ofNullable(userDetails).isPresent()) {
            throw new GlobalDefaultException(1000);
        }
        long marketId = marketServiceClient.getBySymbol(form.getSymbol());

        if (marketId == 0L) {
            throw new GlobalDefaultException(10002);
        }
        //查找交易市场是否收藏
        QueryWrapper<UserFavoriteMarket> e = new QueryWrapper<>();
        e.eq("market_id", marketId);
        e.eq("user_id", userDetails.getId());
        UserFavoriteMarket f = userFavoriteMarketService.selectOne(e);
        if (Optional.ofNullable(f).isPresent()) {
            throw new GlobalDefaultException(20011);
        }

        UserFavoriteMarket u = new UserFavoriteMarket();
        u.setMarketId(marketId).setType(form.getType()).setUserId(userDetails.getId());
        userFavoriteMarketService.insert(u);
        return Response.ok();
    }

    @PostMapping("/deleteFavorite/{symbol}/{type}")
    @ApiOperation(value = "撤销用户交易市场", notes = "撤销用户交易市场", httpMethod = "DELETE", authorizations = {@Authorization(value = "Authorization")})
    public Object deleteFavorite(@PathVariable String symbol, @PathVariable long type, @ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        if (!Optional.ofNullable(userDetails).isPresent()) {
            throw new GlobalDefaultException(1000);
        }
        long marketId = marketServiceClient.getBySymbol(symbol);
        QueryWrapper<UserFavoriteMarket> e = new QueryWrapper<>();
        e.eq("market_id", marketId);
        e.eq("type", type);
        e.eq("user_id", userDetails.getId());
        userFavoriteMarketService.delete(e);
        return Response.ok();
    }
}