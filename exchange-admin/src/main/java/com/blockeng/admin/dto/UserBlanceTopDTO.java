package com.blockeng.admin.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 用户持仓排行DTO
 * </p>
 *
 * @author Haliyo
 * @since 2018-05-13
 */
@Data
@Accessors(chain = true)
public class UserBlanceTopDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "用户ID", name = "userId", required = false)
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "持有BTC", name = "btcAmount", required = false)
    @TableField("btc_amount")
    private BigDecimal btcAmount;

    @ApiModelProperty(value = "持有ETH", name = "ethAmount", required = false)
    @TableField("eth_amount")
    private BigDecimal ethAmount;

    @ApiModelProperty(value = "持有LTC", name = "ltcAmount", required = false)
    @TableField("ltc_amount")
    private BigDecimal ltcAmount;

    @ApiModelProperty(value = "持有USDT", name = "usdtAmount", required = false)
    @TableField("usdt_amount")
    private BigDecimal usdtAmount;

}
