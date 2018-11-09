package com.blockeng.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blockeng.dto.WithdrawAddressDTO;
import com.blockeng.entity.UserWallet;
import com.blockeng.framework.exception.GlobalDefaultException;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.security.UserDetails;
import com.blockeng.service.UserWalletService;
import com.blockeng.user.dto.UserDTO;
import com.blockeng.user.feign.UserServiceClient;
import com.blockeng.vo.DeleteWalletForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Optional;

/**
 * @Description: 用户提币钱包地址
 * @Author: Chen Long
 * @Date: Created in 2018/5/22 下午3:04
 * @Modified by: Chen Long
 */
@RestController
@RequestMapping("/user/wallet")
@Api(value = "提币钱包地址", description = "提币钱包地址 REST API")
public class UserWalletController {

    @Autowired
    private UserWalletService userWalletService;

    @Autowired
    private UserServiceClient userServiceClient;

    /**
     * 添加提币钱包地址
     *
     * @param withdrawAddress 添加提币地址请求参数
     * @param userDetails     登录用户信息
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/address")
    @ApiOperation(value = "WITHDRAW-ADDRESS-001 添加提币地址", notes = "添加提币地址", httpMethod = "POST",
            authorizations = {@Authorization(value = "Authorization")})
    @ApiImplicitParam(name = "withdrawAddress", value = "添加提币地址请求参数", required = true, dataType = "WithdrawAddressDTO", paramType = "body")
    public Object addAddress(@RequestBody WithdrawAddressDTO withdrawAddress,
                             @ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        if (!Optional.ofNullable(userDetails).isPresent()) {
            throw new GlobalDefaultException(1000);
        }
        userWalletService.addAddress(withdrawAddress, userDetails.getId());
        return Response.ok();
    }


    /**
     * 删除提币钱包地址
     *
     * @param userDetails
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/deleteAddress")
    @ApiOperation(value = "删除钱包地址", notes = "删除钱包地址", httpMethod = "POST",
            authorizations = {@Authorization(value = "Authorization")})
    public Object deleteAddress(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails, @RequestBody DeleteWalletForm form) {
        UserDTO userDTO = userServiceClient.selectById(userDetails.getId());
        if (!new BCryptPasswordEncoder().matches(form.getPayPassword(), userDTO.getPaypassword())) {
            throw new GlobalDefaultException(50020);
        }

        boolean flag = userWalletService.deleteById(form.getAddressId());
        if (!flag) {
            throw new GlobalDefaultException(20009);
        }
        return Response.ok();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getCoinAddress/{coinId}")
    @ApiOperation(value = "用户的提币地址", httpMethod = "GET", authorizations = {@Authorization(value = "Authorization")})
    public Object selectUserWalletList(@PathVariable long coinId, @ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        QueryWrapper<UserWallet> e = new QueryWrapper<>();
        e.eq("user_id", userDetails.getId());
        e.eq("coin_id", coinId);
        return Response.ok(userWalletService.selectList(e));
    }
}
