package com.blockeng.admin.web.sys.role;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.entity.SysRole;
import com.blockeng.admin.entity.SysUser;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.SysRoleService;
import com.google.common.base.Strings;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.Optional;

/**
 * <p>
 * 角色 前端控制器
 * </p>
 *
 * @author Haliyo
 * @since 2018-03-06
 */
@RestController
@RequestMapping("/roles")
@Api(value = "角色管理", tags = "角色管理")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @Log(value = "查询角色列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('sys_role_query')")
    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "每页显示条数，默认 10", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "角色名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "角色代码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态0:禁用 1:启用", dataType = "Integer", paramType = "query"),
    })
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "按条件分页查询角色列表", httpMethod = "GET")
    public Object selectPage(@ApiIgnore Page<SysRole> page, String name,
                             String code, Integer status) {
        EntityWrapper<SysRole> ew = new EntityWrapper<>();

        if (!Strings.isNullOrEmpty(name)) {
            ew.like("name", name);
        }
        if (!Strings.isNullOrEmpty(code)) {
            ew.like("code", code);
        }
        if (Optional.ofNullable(status).isPresent()) {
            ew.eq("status", status);
        }
        ew.orderBy("id", false);
        return ResultMap.getSuccessfulResult(sysRoleService.selectPage(page, ew));
    }

    @Log(value = "查询角色信息", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('sys_role_query')")
    @GetMapping("/{roleId}")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "角色信息", httpMethod = "GET")
    public Object selectOne(@PathVariable String roleId) {
        return ResultMap.getSuccessfulResult(sysRoleService.selectById(roleId));
    }

    @Log(value = "新建角色", type = SysLogTypeEnum.INSERT)
    @PreAuthorize("hasAuthority('sys_role_create')")
    @PostMapping
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "新建角色", httpMethod = "POST")
    public Object insert(@ApiIgnore @AuthenticationPrincipal SysUser sysUser, @RequestBody SysRole sysRole) {
        sysRole.setCreateBy(sysUser.getId());
        if (sysRole != null && StringUtils.isEmpty(sysRole.getName())) {
            return ResultMap.getFailureResult("角色名称不能为空！");
        } else {
            SysRole sysRole1 = sysRoleService.selectOne(new EntityWrapper<SysRole>().eq("name", sysRole.getName()));
            if (sysRole1 != null) {
                return ResultMap.getFailureResult("角色名称已存在");
            }
        }
        if (sysRole != null && StringUtils.isEmpty(sysRole.getCode())) {
            return ResultMap.getFailureResult("角色编码不能为空！");
        } else {
            SysRole sysRole1 = sysRoleService.selectOne(new EntityWrapper<SysRole>().eq("code", sysRole.getCode()));
            if (sysRole1 != null) {
                return ResultMap.getFailureResult("角色编码已使用，请更换一个");
            }
        }

        if (sysRoleService.insert(sysRole)) {
            return ResultMap.getSuccessfulResult("操作成功!");
        } else {
            return ResultMap.getFailureResult("操作失败!");
        }
    }

    @Log(value = "删除角色", type = SysLogTypeEnum.DELETE)
    @PreAuthorize("hasAuthority('sys_role_delete')")
    @RequestMapping({"/delete"})
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "删除角色", httpMethod = "POST")
    public Object delete(@RequestBody String[] ids) {
        if (null == ids || ids.length <= 0) {
            return ResultMap.getFailureResult("参数不能为空！");
        }
        if (sysRoleService.deleteBatchIds(Arrays.asList(ids))) {
            return ResultMap.getSuccessfulResult("操作成功!");
        } else {
            return ResultMap.getFailureResult("操作失败!");
        }
    }
}
