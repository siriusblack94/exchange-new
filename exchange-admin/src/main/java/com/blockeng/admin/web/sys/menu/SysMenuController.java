package com.blockeng.admin.web.sys.menu;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.entity.SysMenu;
import com.blockeng.admin.entity.SysUser;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.SysMenuService;
import com.google.common.base.Strings;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;

/**
 * <p>
 * 系统菜单 前端控制器
 * </p>
 *
 * @author Haliyo
 * @since 2018-03-06
 */
@RestController
@RequestMapping("/menu")
@Api(value = "系统菜单", tags = "系统菜单")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @Log(value = "查询系统菜单列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('menu_query')")
    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "每页显示条数，默认 10", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "菜单名称", dataType = "String", paramType = "query"),
    })
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "按条件分页查询系统菜单列表", httpMethod = "GET")
    public Object selectPage(@ApiIgnore Page<SysMenu> page, String name) {
        EntityWrapper<SysMenu> ew = new EntityWrapper<>();
        if (!Strings.isNullOrEmpty(name)) {
            ew.like("name", name);
        }
        return ResultMap.getSuccessfulResult(sysMenuService.selectPage(page, ew));
    }

    @Log(value = "系统菜单信息", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('menu_query')")
    @GetMapping("/{menuId}")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "系统菜单信息", httpMethod = "GET")
    public Object selectOne(@PathVariable String menuId) {
        return ResultMap.getSuccessfulResult(sysMenuService.selectById(menuId));
    }

    @Log(value = "新增系统菜单", type = SysLogTypeEnum.INSERT)
    @PreAuthorize("hasAuthority('menu_create')")
    @PostMapping
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "新增系统菜单", httpMethod = "POST")
    public Object insert(@ApiIgnore @AuthenticationPrincipal SysUser user, @RequestBody SysMenu sysMenu) {
        sysMenu.setCreateBy(user.getId());
        if (sysMenuService.insert(sysMenu)) {
            return ResultMap.getSuccessfulResult("操作成功!");
        } else {
            return ResultMap.getFailureResult("操作失败!");
        }
    }

    @Log(value = "编辑菜单", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('menu_update')")
    @RequestMapping({"/update"})
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "编辑菜单", httpMethod = "PUT")
    public Object update(@ApiIgnore @AuthenticationPrincipal SysUser sysUser, @RequestBody SysMenu sysMenu) {
        sysMenu.setModifyBy(sysUser.getId());
        if (sysMenuService.updateById(sysMenu)) {
            return ResultMap.getSuccessfulResult("操作成功!");
        } else {
            return ResultMap.getFailureResult("操作失败!");
        }
    }

    @Log(value = "删除系统菜单", type = SysLogTypeEnum.DELETE)
    @PreAuthorize("hasAuthority('menu_delete')")
    @RequestMapping({"/delete"})
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "删除系统菜单", httpMethod = "POST")
    public Object delete(@RequestBody String[] ids) {
        if (null == ids || ids.length <= 0) {
            return ResultMap.getFailureResult("参数不能为空！");
        }
        if (sysMenuService.deleteBatchIds(Arrays.asList(ids))) {
            return ResultMap.getSuccessfulResult("操作成功!");
        } else {
            return ResultMap.getFailureResult("操作失败!");
        }
    }
}
