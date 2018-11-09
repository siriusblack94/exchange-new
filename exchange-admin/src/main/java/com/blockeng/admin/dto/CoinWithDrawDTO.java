package com.blockeng.admin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 提币
 * Create Time: 2018年05月31日 18:22
 *
 * @author ch
 * @Dec
 **/
@Data
@Accessors(chain = true)
public class CoinWithDrawDTO {

    @ApiModelProperty(value = "交易id", name = "sumNum", required = true)
    private String txid;//交易id


    @ApiModelProperty(value = "链上手续费", name = "chainFee", required = true)
    private BigDecimal chainFee;//链上手续费


    @ApiModelProperty(value = "id", name = "id", required = true)
    private Integer id;//链上手续费

}
