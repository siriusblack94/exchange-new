package com.blockeng.admin.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * 场外交易 提现统计 新
 * */
@Data
@Accessors(chain = true)
public class CurbExchangeWithdrawStatisticsDTO extends PageDTO{

    @ApiModelProperty(value = "提现金额", name = "withdrawAmount", required = false)
    private BigDecimal withdrawAmount;

    @ApiModelProperty(value = "到账金额", name = "transferAmount", required = false)
    private BigDecimal transferAmount;

    @ApiModelProperty(value = "提现次数", name = "withdrawTimes", required = false)
    private int withdrawTimes;

    @ApiModelProperty(value = "手续费", name = "fee", required = false)
    private BigDecimal fee;

    public CurbExchangeWithdrawStatisticsDTO (){
        setRecords(new ArrayList());
    }

}
