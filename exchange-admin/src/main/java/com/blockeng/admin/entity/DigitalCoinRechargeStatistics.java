package com.blockeng.admin.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class DigitalCoinRechargeStatistics {

    @ApiModelProperty(value = "用户ID", name = "userId", required = false)
    private String userId;

    @ApiModelProperty(value = "币种", name = "coinName", required = false)
    private String coinName;

    @ApiModelProperty(value = "充值数量", name = "rechargeCount", required = false)
    private BigDecimal rechargeCount;

    @ApiModelProperty(value = "方式", name = "rechargeMethod", required = false)
    private String rechargeMethod;

    @ApiModelProperty(value = "充值次数", name = "rechargeTimes", required = false)
    private Integer rechargeTimes;
}
