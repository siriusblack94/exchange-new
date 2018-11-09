package com.blockeng.admin.web.operated;

import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.CommonUtils;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.dto.UserWorkIssueDTO;
import com.blockeng.admin.entity.SysUser;
import com.blockeng.admin.entity.WorkIssue;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.WorkIssueService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 工单记录 前端控制器
 * </p>
 *
 * @author lxl
 * @since 2018-05-13
 */
@Slf4j
@Api(value = "工单记录controller", tags = {"工单"})
@RestController
@RequestMapping("/workIssue")
public class WorkIssueController {

    @Autowired
    private WorkIssueService workIssueService;

    @Log(value = "查询工单列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('work_issue_query')")
    @GetMapping
    @RequestMapping({"/getList"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "工单列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "状态：1-待回答；2-已回答", required = false, dataType = "String"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = false, dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = false, dataType = "String"),
            @ApiImplicitParam(name = "current", value = "当前页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = UserWorkIssueDTO.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap getList(
            int current,
            int size,
            String status,
            @RequestParam(value = "startTime", defaultValue = "") String startTime,
            @RequestParam(value = "endTime", defaultValue = "") String endTime) {
        Map<String, Object> paramMap = new HashMap<>();
        Page<UserWorkIssueDTO> pager = new Page<>(current, size);
        paramMap.put("status", status);
        paramMap.put("startTime", startTime);
        if (StringUtils.isNotBlank(endTime)) {
            endTime = endTime + " 23:59:59";
        }
        paramMap.put("endTime", endTime);
        return ResultMap.getSuccessfulResult(workIssueService.selectMapPage(pager, paramMap));
    }

    @Log(value = "查看单个工单信息", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('work_issue_query')")
    @GetMapping
    @RequestMapping({"/getOneObj"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "单个工单信息", httpMethod = "GET")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = UserWorkIssueDTO.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap getOneObj(Long id) {
        log.info("WorkIssueController getOneObj id:" + id);
        if (null == id) {
            return ResultMap.getFailureResult("参数不能为空！");
        }
        UserWorkIssueDTO userWorkIssueDTO = workIssueService.selectOneObj(id);
        return ResultMap.getSuccessfulResult(userWorkIssueDTO);
    }

    @Log(value = "回复工单", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('work_issue_update')")
    @PostMapping
    @RequestMapping({"/updateAnswer"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "回复", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "answer", value = "回答内容", required = true, dataType = "String")
    })
    public ResultMap updateAnswer(String answer, Long id) {
        log.info("WorkIssueController getOneObj answer:" + answer);
        log.info("WorkIssueController getOneObj id:" + id);
        if (null == id) {
            return ResultMap.getFailureResult("id不能为空！");
        }
        if (StringUtils.isBlank(answer)) {
            return ResultMap.getFailureResult("回答内容不能为空！");
        }
        if (!StringUtils.isEmpty(answer) && answer.length() > 255) {
            return ResultMap.getFailureResult("回答内容长度不能超过255！");
        }
        SysUser loginUser = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        WorkIssue workIssue = new WorkIssue();
        workIssue.setId(id);
        workIssue.setAnswer(answer);
        workIssue.setAnswerName("客服中心");
        workIssue.setStatus(CommonUtils.ANSWER_2);
        workIssue.setAnswerUserId(loginUser.getId());
        Boolean res = workIssueService.updateById(workIssue);
        if (res) {
            return ResultMap.getSuccessfulResult("回复成功");
        } else {
            return ResultMap.getFailureResult("回复失败");
        }
    }
}
