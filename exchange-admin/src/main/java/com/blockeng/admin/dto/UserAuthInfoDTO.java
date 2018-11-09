package com.blockeng.admin.dto;

import com.blockeng.admin.entity.User;
import com.blockeng.admin.entity.UserAuthAuditRecord;
import com.blockeng.admin.entity.UserAuthInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 用户认证实体
 * Create Time: 2018年05月19日 18:39
 * C@author lxl
 **/
@Data
@Accessors(chain = true)
public class UserAuthInfoDTO implements Serializable {


    @ApiModelProperty(value = "审核对象", name = "userAuthAuditRecord", required = false)
    UserAuthAuditRecord userAuthAuditRecord;
    @ApiModelProperty(value = "用户对象", name = "User", required = false)
    User user;
    @ApiModelProperty(value = "用户认证信息集合", name = "List<UserAuthInfo>", required = false)

    List<UserAuthInfo> userAuthInfoList;

}
