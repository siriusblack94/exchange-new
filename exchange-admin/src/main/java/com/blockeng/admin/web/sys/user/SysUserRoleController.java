package com.blockeng.admin.web.sys.user;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.entity.SysUserRole;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.SysUserRoleService;
import com.blockeng.admin.web.vo.GrantRolesForm;
import com.blockeng.admin.web.vo.mappers.GrantRolesMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 系统配置-员工角色管理 前端控制器
 * </p>
 *
 * @author qiang
 * @since 2018-03-06
 */
@RestController
@RequestMapping
@Api(value = "系统配置-员工角色管理", tags = "系统配置-员工角色管理")
public class SysUserRoleController {

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Log(value = "查询员工角色列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('sys_user_query')")
    @GetMapping("/user_roles")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "查询员工角色列表", httpMethod = "GET")
    public Object selectList(Long roleId) {
        EntityWrapper<SysUserRole> ew = new EntityWrapper<>();
        if (Optional.ofNullable(roleId).isPresent()) {
            ew.eq("role_id", roleId);
        }
        return sysUserRoleService.selectList(ew);
    }


    @Log(value = "授予员工角色", type = SysLogTypeEnum.INSERT)
    @PreAuthorize("hasAuthority('sys_user_update')")
    @PostMapping("/grant_roles")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "授予角色权限", httpMethod = "POST")
    Object grant_roles(@RequestBody GrantRolesForm form) {
        List<SysUserRole> sysUserRoles = GrantRolesMapper.convertRoleList(form);
        return ResultMap.getSuccessfulResult(sysUserRoleService.grantRoles(form.getUserId(), sysUserRoles));
    }
}