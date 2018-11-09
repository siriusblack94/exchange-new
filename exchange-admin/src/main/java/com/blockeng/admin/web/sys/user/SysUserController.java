package com.blockeng.admin.web.sys.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.MD5Utils;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.entity.SysUser;
import com.blockeng.admin.entity.SysUserRole;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.SysUserRoleService;
import com.blockeng.admin.service.SysUserService;
import com.google.common.base.Strings;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 员工管理 前端控制器
 * </p>
 *
 * @author Haliyo
 * @since 2018-03-06
 */
@RestController
@RequestMapping("/users")
@Api(value = "员工管理", tags = "员工管理")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Log(value = "查询员工列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('sys_user_query')")
    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "每页显示条数，默认 10", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "fullname", value = "姓名", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "mobile", value = "手机号码", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态 0-无效； 1-有效；", required = false, dataType = "String", paramType = "query"),
    })
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "按条件分页查询员工信息", httpMethod = "GET")
    public Object selectPage(@ApiIgnore Page<SysUser> page,
                             String fullname,
                             String mobile,
                             Integer status) {
        EntityWrapper<SysUser> ew = new EntityWrapper<>();
        //去除了密码这些敏感值
        String sql = "id, username, fullname, mobile, email, status, create_by AS createBy, modify_by AS modifyBy, created, last_update_time AS lastUpdateTime";
        ew.setSqlSelect(sql);
        if (!Strings.isNullOrEmpty(fullname)) {
            ew.like("fullname", fullname)
                    .or().like("id", fullname)
                    .or().like("email", fullname)
                    .or().like("username", fullname);
        }
        if (!Strings.isNullOrEmpty(mobile)) {
            ew.like("mobile", mobile);
        }
        if (Optional.ofNullable(status).isPresent()) {
            ew.eq("status", status);
        }
        return ResultMap.getSuccessfulResult(sysUserService.selectSysUserPage(page, ew));
    }

    @Log(value = "查询员工信息", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('sys_user_query')")
    @GetMapping("/{userId}")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "员工信息", httpMethod = "GET")
    public Object selectOne(@PathVariable String userId) {
        return ResultMap.getSuccessfulResult(sysUserService.selectById(userId));
    }

    @Log(value = "添加员工", type = SysLogTypeEnum.INSERT)
    @PreAuthorize("hasAuthority('sys_user_query')")
    @RequestMapping({"/add"})
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "添加员工", httpMethod = "POST")
    public Object insert(@ApiIgnore @AuthenticationPrincipal SysUser loginUser, @RequestBody SysUser sysUser) {


        sysUser.setCreateBy(loginUser.getId());
        sysUser.setPassword(new BCryptPasswordEncoder().encode(MD5Utils.getMD5(sysUser.getPassword())));
        if (sysUser != null && !StringUtils.isNotBlank(sysUser.getUsername())) {
            return ResultMap.getFailureResult("用户名称不能为空！");
        } else {
            SysUser sysUser1 = sysUserService.selectOne(new EntityWrapper<SysUser>().eq("username", sysUser.getUsername()));
            if (sysUser1 != null) {
                return ResultMap.getFailureResult("用户名已存在，请换个名称！");
            }
        }
        if (sysUser != null && !StringUtils.isNotBlank(sysUser.getMobile())) {
            return ResultMap.getFailureResult("电话不能为空！");
        } else {
            SysUser sysUser1 = sysUserService.selectOne(new EntityWrapper<SysUser>().eq("mobile", sysUser.getMobile()));

            if (sysUser1 != null) {
                return ResultMap.getFailureResult("电话已存在，请换个电话！");
            }
        }
        if (sysUser != null && !StringUtils.isNotBlank(sysUser.getEmail())) {
            return ResultMap.getFailureResult("邮箱不能为空！");
        } else {
            SysUser sysUser2 = sysUserService.selectOne(new EntityWrapper<SysUser>().eq("email", sysUser.getEmail()));
            if (sysUser2 != null) {
                return ResultMap.getFailureResult("邮箱已存在，请换个邮箱！");
            }
        }


        if (sysUserService.insert(sysUser)) {
            if (!Strings.isNullOrEmpty(sysUser.getRoleStrings())) {
                String[] roles = sysUser.getRoleStrings().split(",");
                List<SysUserRole> sysUserRoles = new ArrayList<>();
                for (String roleId : roles) {
                    sysUserRoles.add(new SysUserRole()
                            .setUserId(sysUser.getId())
                            .setRoleId(Long.valueOf(roleId))
                            .setCreateBy(loginUser.getCreateBy())
                    );
                }
                sysUserRoleService.insertBatch(sysUserRoles);
            }
            return ResultMap.getSuccessfulResult("操作成功!");
        } else {
            return ResultMap.getFailureResult("操作失败!");
        }
    }

    @Log(value = "编辑员工", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('sys_user_update')")
    @RequestMapping({"/update"})
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "编辑员工", httpMethod = "PUT")
    public Object update(@RequestBody SysUser sysUser) {
        SysUser loginUser = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (null == loginUser) {
            return ResultMap.getFailureResult("未获取到登陆用户!");
        }
        sysUser.setModifyBy(loginUser.getId());
        SysUser oldsysuser = sysUserService.selectById(sysUser.getId());
        if (oldsysuser == null) {
            return ResultMap.getFailureResult("用户不存在!");
        }
        if (sysUser != null && !StringUtils.isNotBlank(sysUser.getUsername())) {
            return ResultMap.getFailureResult("用户名称不能为空！");
        } else {
            SysUser sysUser1 = sysUserService.selectOne(new EntityWrapper<SysUser>().eq("username", sysUser.getUsername()));
            if (sysUser1 != null && !sysUser.getUsername().equals(oldsysuser.getUsername())) {
                return ResultMap.getFailureResult("用户名已存在，请换个名称！");
            }
        }
        if (sysUser != null && !StringUtils.isNotBlank(sysUser.getMobile())) {
            return ResultMap.getFailureResult("电话不能为空！");
        } else {
            SysUser sysUser1 = sysUserService.selectOne(new EntityWrapper<SysUser>().eq("mobile", sysUser.getMobile()));

            if (sysUser1 != null && !sysUser.getMobile().equals(oldsysuser.getMobile())) {
                return ResultMap.getFailureResult("电话已存在，请换个电话！");
            }
        }
        if (sysUser != null && !StringUtils.isNotBlank(sysUser.getEmail())) {
            return ResultMap.getFailureResult("邮箱不能为空！");
        } else {
            SysUser sysUser2 = sysUserService.selectOne(new EntityWrapper<SysUser>().eq("email", sysUser.getEmail()));
            if (sysUser2 != null && !sysUser.getEmail().equals(oldsysuser.getEmail())) {
                return ResultMap.getFailureResult("邮箱已存在，请换个邮箱！");
            }
        }

        if (!sysUser.getPassword().equals(oldsysuser.getPassword())) {
            sysUser.setPassword(new BCryptPasswordEncoder().encode(MD5Utils.getMD5(sysUser.getPassword())));

        }

        if (sysUserService.updateById(sysUser)) {
            if (!Strings.isNullOrEmpty(sysUser.getRoleStrings())) {
                EntityWrapper<SysUserRole> ew = new EntityWrapper<>();
                ew.eq("user_id", sysUser.getId());
                sysUserRoleService.delete(ew);
                String[] roles = sysUser.getRoleStrings().split(",");
                List<SysUserRole> sysUserRoles = new ArrayList<>();
                for (String roleId : roles) {
                    sysUserRoles.add(new SysUserRole()
                            .setUserId(sysUser.getId())
                            .setRoleId(Long.valueOf(roleId))
                            .setCreateBy(loginUser.getCreateBy())
                    );
                }
                sysUserRoleService.insertBatch(sysUserRoles);
            }
            return ResultMap.getSuccessfulResult("操作成功!");
        } else {
            return ResultMap.getFailureResult("操作失败!");
        }

    }

    @Log(value = "删除员工", type = SysLogTypeEnum.DELETE)
    @PreAuthorize("hasAuthority('sys_user_delete')")
    @RequestMapping({"/delete"})
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "删除员工", httpMethod = "POST")
    public Object delete(@RequestBody String[] ids) {
        if (null == ids || ids.length <= 0) {
            return ResultMap.getFailureResult("参数不能为空！");
        }
        if (sysUserService.deleteBatchIds(Arrays.asList(ids))) {
            return ResultMap.getSuccessfulResult("操作成功!");
        } else {
            return ResultMap.getFailureResult("操作失败!");
        }
    }

}