package com.blockeng.admin.dto;

import com.blockeng.admin.entity.WorkIssue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 工单记录
 * Create Time: 2018年05月21日 14:40
 * C@author lxl
 **/
@Data
@Accessors(chain = true)
public class UserWorkIssueDTO extends WorkIssue {
    @ApiModelProperty(value = "用户名称；", name = "username", required = false)
    public String username;//用户名称
    @ApiModelProperty(value = "真实名称", name = "realName", required = false)
    public String realName;//真实名称
}
