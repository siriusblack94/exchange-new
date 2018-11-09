package com.blockeng.admin.entity;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class DigitalCoinWithdrawStatistics {

    @ApiModelProperty(value = "用户ID", name = "userId", required = false)
    private String userId;

    @ApiModelProperty(value = "币种", name = "coinName", required = false)
    private String coinName;

    @ApiModelProperty(value = "提币数量", name = "withdrawCount", required = false)
    private BigDecimal withdrawCount;

    @ApiModelProperty(value = "手续费", name = "fee", required = false)
    private BigDecimal fee;

    @ApiModelProperty(value = "实际提币数量", name = "realWithdrawCount", required = false)
    private BigDecimal realWithdrawCount;

    @ApiModelProperty(value = "提现次数", name = "withdrawTimes", required = false)
    private Integer withdrawTimes;

}
