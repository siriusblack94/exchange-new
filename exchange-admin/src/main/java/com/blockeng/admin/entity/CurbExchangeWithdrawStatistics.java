package com.blockeng.admin.entity;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 *  场外交易提现
 * */
@Data
@Accessors(chain = true)
public class CurbExchangeWithdrawStatistics {

    @ApiModelProperty(value = "用户ID", name = "userId", required = false)
    private String userId;

    @ApiModelProperty(value = "提现金额", name = "withdrawAmount", required = false)
    private BigDecimal withdrawAmount;

    @ApiModelProperty(value = "手续费", name = "fee", required = false)
    private BigDecimal fee;

    @ApiModelProperty(value = "到账金额", name = "transferAmount", required = false)
    private BigDecimal transferAmount;

    @ApiModelProperty(value = "提现金额", name = "withdrawTimes", required = false)
    private Integer withdrawTimes;
}
