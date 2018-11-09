package com.blockeng.admin.web.sys.log;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.dto.SysUserLogDTO;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.SysUserLogService;
import com.google.common.base.Strings;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 系统日志 前端控制器
 * </p>
 *
 * @author qiang
 * @since 2018-05-11
 */
@RestController
@RequestMapping("/sysUserLog")
@Slf4j
@Api(value = "系统日志", tags = {"系统日志"})
public class SysUserLogController {

    @Autowired
    private SysUserLogService sysUserLogService;


    @PreAuthorize("hasAuthority('sys_user_log_query')")
    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "每页显示条数，默认 10", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userName", value = "用户姓名", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String")
    })
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "按条件分页系统日志列表列表", httpMethod = "GET")
    public Object selectPage(@RequestParam(value = "current", defaultValue = "1") int current,
                             @RequestParam(value = "size", defaultValue = "10") int size,
                             String userName,
                             String startTime,
                             String endTime) {
        EntityWrapper<SysUserLogDTO> ew = new EntityWrapper<>();
        Page<SysUserLogDTO> pager = new Page<>(current, size);
        if (StringUtils.isNotBlank(endTime)) {
            endTime = endTime + " 23:59:59";
        }
        if (!Strings.isNullOrEmpty(userName)) {
            ew.like("su.userName", userName);
        }
        if (!Strings.isNullOrEmpty(startTime)) {
            ew.ge("sl.created", startTime);
        }
        if (!Strings.isNullOrEmpty(endTime)) {
            ew.le("sl.created", endTime);
        }
        return ResultMap.getSuccessfulResult(sysUserLogService.selectListPage(pager, ew));
    }


}

