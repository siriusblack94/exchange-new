package com.blockeng.admin.web.sys.role;

import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.entity.SysMenu;
import com.blockeng.admin.entity.SysRoleMenu;
import com.blockeng.admin.entity.SysRolePrivilege;
import com.blockeng.admin.entity.SysUser;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.SysMenuService;
import com.blockeng.admin.service.SysRolePrivilegeService;
import com.blockeng.admin.web.vo.GrantPrivilegesForm;
import com.blockeng.admin.web.vo.mappers.GrantPrivilegesMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * <p>
 * 角色权限配置 前端控制器
 * </p>
 *
 * @author qiang
 * @since 2018-03-06
 */
@RestController
@RequestMapping
@Api(value = "角色权限配置", tags = "角色权限配置")
public class SysRolePrivilegeController {

    @Autowired
    private SysRolePrivilegeService sysRolePrivilegeService;

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private GrantPrivilegesMapper grantPrivilegesMapper;

    @Log(value = "查询角色权限配置列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('sys_role_query')")
    @GetMapping
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "查询角色权限配置列表", httpMethod = "GET")
    @RequestMapping("/role_privileges")
    public Object selectList(@ApiIgnore @AuthenticationPrincipal SysUser sysUser, @RequestParam Long roleId) {
        List<SysMenu> menus = sysMenuService.childs(sysMenuService.selectPrivilegeMenuList(roleId,sysUser.getId()));
        return ResultMap.getSuccessfulResult(menus);
    }

    @Log(value = "授予角色权限", type = SysLogTypeEnum.INSERT)
    @PreAuthorize("hasAuthority('sys_role_update')")
    @PostMapping("/grant_privileges")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "授予角色权限", httpMethod = "POST")
    public Object grant_privileges(@RequestBody GrantPrivilegesForm form) {
        List<Object> data = grantPrivilegesMapper.convertPrivilegeList(form);
        List<SysRolePrivilege> sysRolePrivileges = (List<SysRolePrivilege>) data.get(0);
        List<SysRoleMenu> sysRoleMenus = (List<SysRoleMenu>) data.get(1);
        return ResultMap.getSuccessfulResult(sysRolePrivilegeService.grantPrivileges(form.getRoleId(), sysRolePrivileges, sysRoleMenus));
    }
}