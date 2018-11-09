package com.blockeng.web;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.security.UserDetails;
import com.blockeng.repository.UserLoginLogRepository;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <p>
 * 用户登录日志 前端控制器
 * </p>
 *
 * @author qiang
 * @since 2018-03-06
 */
@RestController
@RequestMapping("/login/history")
@Api(value = "用户登录日志", tags = "用户登录日志")
public class UserLoginLogController {

    @Autowired
    private UserLoginLogRepository userLoginLogRepository;

    /**
     * 登录历史
     *
     * @param pageable
     * @return
     */
    @ApiOperation(value = "登录历史", httpMethod = "GET", authorizations = {@Authorization(value = "Authorization")})
    @ApiResponses(value = {
            @ApiResponse(code = 0, message = "请求成功", response = IPage.class),
    })
    @GetMapping
    Object list(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails, @ApiIgnore Pageable pageable) {

        return Response.ok(userLoginLogRepository.findByUserId(userDetails.getId(), pageable));
    }
}