package com.blockeng.admin.web.sys.login;

import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.entity.SysUser;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.SysUserService;
import com.blockeng.framework.http.Response;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author qiang
 */
@RestController
@RequestMapping
@Slf4j
@Api(value = "管理员登录", tags = {"管理员登录"})
public class SysLoginController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 操作结果
     * @throws AuthenticationException 错误信息
     */
    @Log("管理员登录")
    @PostMapping("/login")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "String", paramType = "query", example = "admin"),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "String", paramType = "query", example = "e10adc3949ba59abbe56e057f20f883e")
    })
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "管理员登录", httpMethod = "POST")
    public Object login(String username, String password) throws AuthenticationException {
        return sysUserService.login(username, password);
    }

    /**
     * 刷新密钥
     *
     * @param authorization 原密钥(Bearer )
     * @return 新密钥
     * @throws AuthenticationException 错误信息
     */
    @Log("刷新密钥")
    @GetMapping(value = "/refreshToken")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "用户名", dataType = "String", paramType = "header")
    })
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "刷新密钥", httpMethod = "GET")
    public Object refreshToken(@RequestHeader String authorization) throws AuthenticationException {
        String jwt = sysUserService.refreshToken(authorization);
        return Response.ok(jwt);
    }

    @Log(value = "获取用户信息", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/getInfo")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "获取用户信息", httpMethod = "GET")
    public Object getInfo(@ApiIgnore @AuthenticationPrincipal SysUser user) {
        return Response.ok(user);
    }
}
