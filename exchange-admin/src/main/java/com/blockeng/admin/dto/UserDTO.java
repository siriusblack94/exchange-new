package com.blockeng.admin.dto;

import com.blockeng.admin.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * Create Time: 2018年06月04日 17:49
 *
 * @author lxl
 * @Dec
 **/
@Data
@Accessors(chain = true)
public class UserDTO extends User {

    @ApiModelProperty(value = "审核状态，0待审核，1审核通过，2审核拒绝", name = "countDate", required = false)
    private Integer userAuthStatus;

    @ApiModelProperty(value = "审核时间", name = "authCreate", required = false)
    private Date authCreate;

    @ApiModelProperty(value = "审核备注", name = "remark", required = false)
    private String remark;


    public void setUserAuthStatus(Integer userAuthStatus) {
        this.userAuthStatus = userAuthStatus;
    }


}
