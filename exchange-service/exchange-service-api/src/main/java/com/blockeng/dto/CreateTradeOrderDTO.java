package com.blockeng.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description: 委托下单
 * @Author: Chen Long
 * @Date: Created in 2018/5/15 上午11:30
 * @Modified by: Chen Long
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CreateTradeOrderDTO implements Serializable {

    /**
     * 交易对标识符
     */
    @NotEmpty(message = "交易对标识符不能为空")
    @ApiModelProperty(value = "交易对标识符", name = "symbol", example = "BTCUSDT")
    private String symbol;

    /**
     * 委托价格
     */
    @NotNull(message = "委托价格不能为空")
    @ApiModelProperty(value = "委托价格", name = "price", example = "7100")
    private BigDecimal price;

    /**
     * 委托数量
     */
    @NotNull(message = "委托数量不能为空")
    @ApiModelProperty(value = "委托数量", name = "volume", example = "1.5")
    private BigDecimal volume;

    /**
     * 委托类型
     */
    @NotNull(message = "委托数量不能为空")
    @ApiModelProperty(value = "委托类型：1-买；2-卖", name = "type", example = "1")
    private Integer type;
}
