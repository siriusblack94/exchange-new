package com.blockeng.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.blockeng.dto.UserBankDTOMapper;
import com.blockeng.entity.User;
import com.blockeng.entity.UserBank;
import com.blockeng.enums.BankStatus;
import com.blockeng.framework.enums.ResultCode;
import com.blockeng.framework.exception.GlobalDefaultException;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.security.UserDetails;
import com.blockeng.service.UserBankService;
import com.blockeng.service.UserService;
import com.blockeng.user.dto.UserBankDTO;
import com.blockeng.web.vo.UserBankForm;
import com.blockeng.web.vo.mappers.UserBankMapper;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

/**
 * <p>
 * 用户绑卡信息表 前端控制器
 * </p>
 *
 * @author qiang
 * @since 2018-03-06
 */
@RestController
@RequestMapping("/v3/cards")
@Api(value = "用户绑卡信息", tags = "用户绑卡信息表")
public class UserBankController {

    @Autowired
    private UserBankService userBankService;

    @Autowired
    private UserService userService;

    /**
     * 绑卡列表
     *
     * @param userDetails
     * @param page
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @ApiOperation(value = "绑卡列表", httpMethod = "GET", authorizations = {@Authorization(value = "Authorization")})
    @ApiResponses(value = {
            @ApiResponse(code = 0, message = "请求成功", response = IPage.class),
    })
    public Object list(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails, @ApiIgnore IPage<UserBank> page) {
        QueryWrapper<UserBank> ew = new QueryWrapper<>();
        ew.eq("user_id", userDetails.getId());
        ew.orderByDesc("created");
        return userBankService.selectPage(page, ew);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/bank")
    @ApiOperation(value = "OUTSIDE-002 C2C个人银行卡", notes = "银行卡", httpMethod = "GET", authorizations = {@Authorization(value = "Authorization")})
    public Object bank(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        if (!Optional.ofNullable(userDetails).isPresent()) {
            return Response.err(ResultCode.USER_NOT_LONG);
        }
        QueryWrapper<UserBank> e = new QueryWrapper<>();
        e.eq("user_id", userDetails.getId());
        e.eq("status", 1);
        return Response.ok(userBankService.selectOne(e));
    }

    /**
     * 添加银行卡
     *
     * @param form
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/bind")
    @ApiOperation(value = "添加银行卡", httpMethod = "POST", authorizations = {@Authorization(value = "Authorization")})
    @ApiResponses(value = {
            @ApiResponse(code = 0, message = "请求成功", response = Response.class),
            @ApiResponse(code = 40003, message = "短信验证码错误", response = Response.class)
    })
    public Object bind(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails,
                       @ApiParam(value = "银行卡信息", required = true) @RequestBody @Valid UserBankForm form) {
        if (!Optional.ofNullable(userDetails).isPresent()) {
            return Response.err(ResultCode.USER_NOT_LONG);
        }
        User user = userService.selectById(userDetails.getId());
        if (!new BCryptPasswordEncoder().matches(form.getPayPassword(), user.getPaypassword())) {
            throw new GlobalDefaultException(2012);
        }
        UserBank userBank = UserBankMapper.INSTANCE.map(form);
        userBank.setUserId(user.getId());
        userBank.setLastUpdateTime(new Date());
        userBank.setCreated(new Date());
        userBank.setStatus(BankStatus.USE.getValue());
        boolean flag;
        if (form.getId() != 0L) {
            flag = userBankService.updateById(userBank);
            if (!flag) {
                throw new GlobalDefaultException(20009);
            }
        } else {
            flag = userBankService.insert(userBank);
            if (!flag) {
                throw new GlobalDefaultException(20008);
            }
        }
        return Response.ok();
    }

    /**
     * 查询用户银行卡
     *
     * @param userId 用户ID
     * @return
     */
    @PostMapping("/selectUserBankByUserId")
    @ApiIgnore
    public UserBankDTO selectUserBankByUserId(Long userId) {
        QueryWrapper<UserBank> ew = new QueryWrapper<>();
        ew.eq("user_id", userId);
        UserBank userBank = userBankService.selectOne(ew);
        return UserBankDTOMapper.INSTANCE.from(userBank);
    }
}