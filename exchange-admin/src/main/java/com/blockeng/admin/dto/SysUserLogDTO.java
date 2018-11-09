package com.blockeng.admin.dto;

import com.blockeng.admin.entity.SysUserLog;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Create Time: 2018年06月04日 17:00
 *
 * @author lxl
 * @Dec
 **/
@Data
@Accessors(chain = true)
public class SysUserLogDTO extends SysUserLog {

    @ApiModelProperty(value = "用户名称", name = "username", required = false)
    public String username;
}
