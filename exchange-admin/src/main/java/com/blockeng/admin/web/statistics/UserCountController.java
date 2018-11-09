package com.blockeng.admin.web.statistics;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.dto.UserCountLoginDTO;
import com.blockeng.admin.dto.UserCountRegDTO;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.UserLoginLogService;
import com.blockeng.admin.service.UserService;
import com.google.common.base.Strings;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户统计 前端控制器
 * </p>
 *
 * @author Haliyo
 * @since 2018-05-13
 */
@Slf4j
@RestController
@RequestMapping("/user/count")
@Api(value = "用户统计", description = "用户统计控制器")
public class UserCountController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserLoginLogService userLoginLogService;

    @Log(value = "按条件分页查询注册统计列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('register_statistics_query')")
    @GetMapping("/reg")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "每页显示条数，默认 10", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String", paramType = "query"),
    })
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "按条件分页查询注册统计列表", httpMethod = "GET")
    public Object selectRegPage(@ApiIgnore Page<UserCountRegDTO> page,
                                String startTime, String endTime) {
        EntityWrapper<UserCountRegDTO> ew = new EntityWrapper<>();
        if (!Strings.isNullOrEmpty(startTime)) {
            ew.ge("b.created", startTime);
        }
        if (!Strings.isNullOrEmpty(endTime)) {
            endTime = endTime + " 23:59:59";
            ew.le("b.created", endTime);
        }
        return ResultMap.getSuccessfulResult(userService.selectRegCountPage(page, ew));
    }

    @Log(value = "按条件分页查询登陆统计列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('login_statistics_query')")
    @GetMapping("/login")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "每页显示条数，默认 10", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String", paramType = "query"),
    })
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "按条件分页查询登陆统计列表", httpMethod = "GET")
    public Object selectLoginPage(@ApiIgnore Page<UserCountLoginDTO> page,
                                  String startTime, String endTime) {
        EntityWrapper<UserCountLoginDTO> ew = new EntityWrapper<>();
        if (!Strings.isNullOrEmpty(startTime)) {
            ew.ge("b.login_date", startTime);
        }
        if (!Strings.isNullOrEmpty(endTime)) {
            endTime = endTime + " 23:59:59";
            ew.le("b.login_date", endTime);
        }
        return ResultMap.getSuccessfulResult(userLoginLogService.selectLoginCountPage(page, ew));
    }

    @Log(value = "登录记录详细列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('login_log_query')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "每页显示条数，默认 10", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String", paramType = "query"),
    })
    @GetMapping("/login/detail")
    public Object selectLoginPage1(int current, int size,
                                   String startTime, String endTime) {

        org.springframework.data.domain.Page dataPage = userLoginLogService.selectListFromMongo(Integer.valueOf(current), Integer.valueOf(size), startTime, endTime);
        Page page = new Page<>();
        List<UserCountLoginDTO> list = new ArrayList<>();
        page.setRecords(dataPage.getContent()).setSize(dataPage.getSize()).setCurrent(dataPage.getNumber() + 1).setTotal(dataPage.getTotalElements());

        return ResultMap.getSuccessfulResult(page);
    }
}
