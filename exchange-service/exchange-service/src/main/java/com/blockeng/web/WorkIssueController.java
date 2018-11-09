package com.blockeng.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blockeng.entity.WorkIssue;
import com.blockeng.framework.enums.WorkIssueStatus;
import com.blockeng.framework.exception.GlobalDefaultException;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.security.UserDetails;
import com.blockeng.service.WorkIssueService;
import com.blockeng.vo.WorkIssueForm;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

/**
 * <p>
 * 工单记录 前端控制器
 * </p>
 *
 * @author qiang
 * @since 2018-05-31
 */
@RestController
@RequestMapping("/workIssue")
@Slf4j
@Api(value = "工单系统", description = "工单系统 REST API")
public class WorkIssueController {

    @Autowired
    private WorkIssueService workIssueService;

    @PostMapping("/addWorkIssue")
    @ApiOperation(value = "添加工单", notes = "添加工单", httpMethod = "POST", authorizations = {@Authorization(value = "Authorization")})
    @PreAuthorize("isAuthenticated()")
    public Object addWorkIssue(@RequestBody @Valid WorkIssueForm workIssueForm, @ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        if (!Optional.ofNullable(userDetails).isPresent()) {
            throw new GlobalDefaultException(1000);
        }
        WorkIssue workIssue = new WorkIssue();
        workIssue.setQuestion(workIssueForm.getQuestion()).
                setUserId(userDetails.getId()).
                setStatus(WorkIssueStatus.NOT_AN.getCode()).
                setCreated(new Date()).
                setLastUpdateTime(new Date());
        boolean flag = workIssueService.insert(workIssue);
        if (!flag) {
            throw new GlobalDefaultException(50053);
        }
        return Response.ok();
    }

    @GetMapping("/issueList/{current}/{size}")
    @ApiOperation(value = "获取工单列表", notes = "获取工单列表", httpMethod = "GET", authorizations = {@Authorization(value = "Authorization")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页码", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示数据量", required = true, dataType = "int", paramType = "path")
    })
    @PreAuthorize("isAuthenticated()")
    public Object issueList(@PathVariable("current") int current,
                            @PathVariable("size") int size,
                            @ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        if (!Optional.ofNullable(userDetails).isPresent()) {
            throw new GlobalDefaultException(1000);
        }
        QueryWrapper<WorkIssue> e = new QueryWrapper<>();
        e.eq("user_id", userDetails.getId());
        e.orderByDesc("created");
        IPage<WorkIssue> page = new Page<>(current, size);
        return Response.ok(workIssueService.selectPage(page, e));
    }
}

