package com.blockeng.admin.entity;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class CurbExchangeRechargeStatistics {


    @ApiModelProperty(value = "用户ID", name = "userId", required = false)
    private String userId;

    @ApiModelProperty(value = "充值金额", name = "rechargeAmount", required = false)
    private BigDecimal rechargeAmount;

    @ApiModelProperty(value = "到账金额", name = "transferAmount", required = false)
    private BigDecimal transferAmount;

    @ApiModelProperty(value = "充值次数", name = "rechargeTimes", required = false)
    private Integer rechargeTimes;

}
