package com.blockeng.admin.dto;

import com.blockeng.admin.entity.CashWithdrawals;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 用户提现表
 * Create Time: 2018年05月16日 21:26
 * C@author lxl
 **/
@Data
@Accessors(chain = true)
public class UserWithDrawalsDTO extends CashWithdrawals {

    @ApiModelProperty(value = "用户名称", name = "username", required = false)
    public String username;//用户名称
    @ApiModelProperty(value = "真实名称", name = "realName", required = false)
    public String realName;//真实名称
    @ApiModelProperty(value = "银行卡名称/卡号", name = "bankNameAndCard", required = false)
    public String bankNameAndCard;//银行卡名称/卡号
    @ApiModelProperty(value = "扣除费率后的钱", name = "realAmount", required = false)
    public BigDecimal realAmount;//扣除费率后的钱

    /**
     * 状态：0，禁用；1，启用；
     */
    @ApiModelProperty(value = "状态值的中文展示", name = "statusStr", required = false)
    private String statusStr;

    /**
     * 0-待审核；1-审核通过；2-拒绝；3-提现成功
     * public
     *
     * @return
     */
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
            return "提现成功";
        }
        return "未知";
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }
}
