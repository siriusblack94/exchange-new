package com.blockeng.mining.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ApiModel(value = "挖矿奖励", description = "挖矿奖励")
public class NowWeekDividendDTO {

    /**
     * 本周可解冻金额
     */
    @ApiModelProperty(name = "totalAmount", value = "总的可以解冻个数", example = "11")
    private BigDecimal totalAmount;

    /**
     * 对应usdt的价格
     */
    @ApiModelProperty(name = "usdtAmount", value = "美元价格", example = "11")
    private BigDecimal usdtAmount;

    /**
     * 对应人民币价格
     */
    @ApiModelProperty(name = "cnyAmount", value = "人民币价格", example = "11")
    private BigDecimal cnyAmount;
}
