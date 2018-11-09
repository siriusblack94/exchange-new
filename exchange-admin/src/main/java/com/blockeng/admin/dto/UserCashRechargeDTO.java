package com.blockeng.admin.dto;

import com.blockeng.admin.entity.CashRecharge;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户充值表
 * Create Time: 2018年05月16日 21:26
 * C@author lxl
 **/
@Data
@Accessors(chain = true)
public class UserCashRechargeDTO extends CashRecharge {
    @ApiModelProperty(value = "用户名称", name = "username", required = false)

    public String username;//用户名称
    @ApiModelProperty(value = "真实名称", name = "realName", required = false)

    public String realName;//真实名称
    @ApiModelProperty(value = "手机号", name = "mobile", required = false)

    public String mobile;//手机号

    @ApiModelProperty(value = "状态值的中文展示", name = "statusStr", required = false)

    private String statusStr;

    public String getStatusStr() {
        if (0 == this.getStatus()) {
            return "待审核";
        }
        if (1 == this.getStatus()) {
            return "审核通过";
        }
        if (2 == this.getStatus()) {
            return "拒绝";
        }
        if (3 == this.getStatus()) {
            return "充值成功";
        }
        return "未知";
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }


}
