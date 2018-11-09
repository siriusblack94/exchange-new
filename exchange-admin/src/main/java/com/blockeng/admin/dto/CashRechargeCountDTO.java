package com.blockeng.admin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * CNY充值统计
 * Create Time: 2018年05月31日 18:22
 *
 * @author lxl
 * @Dec
 **/
@Data
@Accessors(chain = true)
public class CashRechargeCountDTO {

    @ApiModelProperty(value = "充值金额", name = "sumNum", required = false)
    private String sumNum;//充值金额 和
    @ApiModelProperty(value = "到账金额", name = "sumMum", required = false)
    private String sumMum;//到账金额 和
    @ApiModelProperty(value = "充值笔数", name = "counts", required = false)
    private String counts;//充值笔数
    @ApiModelProperty(value = "充值用户数", name = "userCounts", required = false)
    private String userCounts;//充值用户数
    @ApiModelProperty(value = "充值时间", name = "created", required = false)
    private String created;//充值时间
    @ApiModelProperty(value = "成功笔数", name = "validCounts", required = false)
    private String validCounts;//成功笔数
    @ApiModelProperty(value = "用户的id", name = "userId", required = false)
    private String userId;//用户的id
    private String userCt;//充值用户数
}
