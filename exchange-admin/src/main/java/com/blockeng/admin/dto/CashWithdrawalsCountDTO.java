package com.blockeng.admin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * CNY提现统计
 * Create Time: 2018年05月31日 18:22
 *
 * @author lxl
 * @Dec
 **/
@Data
@Accessors(chain = true)
public class CashWithdrawalsCountDTO {

    @ApiModelProperty(value = "提现金额", name = "sumNum", required = false)
    private String sumNum;//提现金额 和
    @ApiModelProperty(value = "到账金额", name = "sumMum", required = false)
    private String sumMum;//到账金额 和
    @ApiModelProperty(value = "提现笔数", name = "counts", required = false)
    private String counts;//提现笔数
    @ApiModelProperty(value = "提现用户数", name = "userCounts", required = false)
    private String userCounts;//提现用户数
    @ApiModelProperty(value = "提现时间", name = "created", required = false)
    private String created;//提现时间
    @ApiModelProperty(value = "提现成功笔数", name = "validCounts", required = false)
    private String validCounts;//提现成功笔数
    @ApiModelProperty(value = "提现用户的id", name = "userId", required = false)
    private String userId;//提现用户的id
    private String userCt;//提现用户数
}
