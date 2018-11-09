package com.blockeng.admin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 虚拟币充值统计
 * Create Time: 2018年05月31日 18:22
 *
 * @author lxl
 * @Dec
 **/
@Data
@Accessors(chain = true)
public class CoinRechargeCountDTO {

    @ApiModelProperty(value = "到账金额", name = "sumAmount", required = false)
    private String sumAmount;//到账金额 和
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
