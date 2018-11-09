package com.blockeng.web;

import com.blockeng.framework.exception.GlobalDefaultException;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.security.JwtToken;
import com.blockeng.framework.security.TokenProvider;
import com.blockeng.framework.security.UserDetails;
import com.blockeng.service.UserDetailsService;
import com.blockeng.web.vo.UserLoginForm;
import com.blueconic.browscap.ParseException;
import com.google.common.base.Strings;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

/**
 * 用户登录
 *
 * @author qiang
 */
@RestController
@RequestMapping
@Slf4j
@Api(value = "用户登录", tags = "用户登录")
public class LoginController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenProvider tokenProvider;



    /**
     * 用户登录
     *
     * @param form 登录表单
     * @return 操作结果
     * @throws AuthenticationException 错误信息
     */
    @PostMapping("/login")
    @ApiResponses(value = {
            @ApiResponse(code = 0, message = "请求成功", response = Response.class),
            @ApiResponse(code = -1, message = "注册失败，系统繁忙", response = Response.class),
            @ApiResponse(code = 40002, message = "用户已存在（手机号码已注册）", response = Response.class)
    })
    @ApiOperation(value = "用户登录", httpMethod = "POST")
    public Object login(@ApiParam(value = "用户登录信息", required = true) @RequestBody @Valid UserLoginForm form, BindingResult result,HttpServletRequest request) throws AuthenticationException, IOException, ParseException {
        //校验用户登录环境
        userDetailsService.check(form);
        UserDetails userDetails = userDetailsService.login(form,request);
        return Response.ok(userDetails);
    }

    /**
     * 刷新密钥
     *
     * @param authorization 原密钥
     * @return 新密钥
     * @throws AuthenticationException 错误信息
     */
    @GetMapping(value = "/refreshToken")
    @ApiOperation(value = "刷新密钥", httpMethod = "GET", authorizations = {@Authorization(value = "Authorization")})
    @PreAuthorize("isAuthenticated()")
    public Object refreshToken(@ApiIgnore @RequestHeader String authorization) throws AuthenticationException {
        String accessToken = tokenProvider.resolveToken(request);
        if (!Strings.isNullOrEmpty(accessToken)) {
            JwtToken jwtToken = userDetailsService.refreshToken(accessToken);
            return Response.ok(jwtToken);
        } else {
            //无效凭证，access_token无效或不是最新的。
            throw new GlobalDefaultException(40001);
        }
    }

    /**
     * 获取access_token（机器人专用）
     *
     * @return
     */
    @GetMapping("/cgi-bin/token")
    @ApiOperation(value = "获取access_token（机器人专用）")
    public Object oauthToken(String access_key, String secret) {
        UserDetails userDetails = userDetailsService.oauth(access_key, secret);
        return Response.ok(userDetails);
    }
}