package com.blockeng.admin.dto;

import com.blockeng.admin.entity.CoinRecharge;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Create Time: 2018年05月20日 15:18
 * C@author lxl
 **/
@Data
@Accessors(chain = true)
public class CoinRechargeDTO extends CoinRecharge {

    private String username;//用户名称

    private String realName;//真实名称

    private String mobile;//手机号

    @ApiModelProperty(value = "状态值的中文展示", name = "statusStr", required = false)
    private String statusStr;

    /**
     * 0-成功1失败
     * public
     *
     * @return
     */
    public String getStatusStr() {
        if (0 == this.getStatus()) {
            return "待入帐";
        }
        if (1 == this.getStatus()) {
            return "充值失败";
        }
        if (2 == this.getStatus()) {
            return "到账失败";
        }
        if (3 == this.getStatus()) {
            return "到账成功";
        }
        return "未知";
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

}
