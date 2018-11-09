package com.blockeng.admin.web.sys.privilege;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.entity.SysPrivilege;
import com.blockeng.admin.entity.SysUser;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.SysPrivilegeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;

/**
 * <p>
 * 权限配置 前端控制器
 * </p>
 *
 * @author qiang
 * @since 2018-03-06
 */
@RestController
@RequestMapping("/privileges")
@Api(value = "权限配置", tags = "权限配置")
public class SysPrivilegeController {

    @Autowired
    private SysPrivilegeService sysPrivilegeService;

    @Log(value = "查询权限配置列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('sys_privilege_query')")
    @GetMapping
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "查询权限配置列表", httpMethod = "GET")
    public Object selectList(@ApiIgnore @AuthenticationPrincipal SysUser sysUser,Page<SysPrivilege> page) {
        Page<SysPrivilege> page1 = new Page<>();
        page1.setCurrent(page.getCurrent());
        page1.setSize(page.getSize());
        page1.setTotal(sysPrivilegeService.selectCountByUserId(sysUser.getId()));
        page.setCurrent(page.getCurrent()-1);
        page1.setRecords(sysPrivilegeService.selectPageByUserId(page,sysUser.getId()));
        return ResultMap.getSuccessfulResult(page1);
    }

    @Log(value = "新增系统权限", type = SysLogTypeEnum.INSERT)
    @PreAuthorize("hasAuthority('sys_privilege_create')")
    @PostMapping
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "新增系统权限", httpMethod = "POST")
    public Object insert(@ApiIgnore @AuthenticationPrincipal SysUser user, @RequestBody SysPrivilege sysPrivilege) {
        sysPrivilege.setCreateBy(user.getId());
        if (sysPrivilegeService.insert(sysPrivilege)) {
            return ResultMap.getSuccessfulResult("操作成功!");
        } else {
            return ResultMap.getFailureResult("操作失败!");
        }
    }

    @Log(value = "编辑系统权限", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('sys_privilege_update')")
    @PatchMapping
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "编辑系统权限", httpMethod = "PATCH")
    public Object update(@ApiIgnore @AuthenticationPrincipal SysUser sysUser, @RequestBody SysPrivilege sysPrivilege) {
        sysPrivilege.setModifyBy(sysUser.getId());
        if (sysPrivilegeService.updateById(sysPrivilege)) {
            return ResultMap.getSuccessfulResult("操作成功!");
        } else {
            return ResultMap.getFailureResult("操作失败!");
        }
    }

    @Log(value = "删除系统权限", type = SysLogTypeEnum.DELETE)
    @PreAuthorize("hasAuthority('sys_privilege_delete')")
    @RequestMapping({"/delete"})
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "删除系统权限", httpMethod = "POST")
    public Object delete(@RequestBody String[] ids) {
        if (null == ids || ids.length <= 0) {
            return ResultMap.getFailureResult("参数不能为空！");
        }
        if (sysPrivilegeService.deleteBatchIds(Arrays.asList(ids))) {
            return ResultMap.getSuccessfulResult("操作成功!");
        } else {
            return ResultMap.getFailureResult("操作失败!");
        }
    }
}
