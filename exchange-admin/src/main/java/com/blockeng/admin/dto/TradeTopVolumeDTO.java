package com.blockeng.admin.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 交易量排行DTO
 * </p>
 *
 * @author Haliyo
 * @since 2018-05-13
 */
@Data
@Accessors(chain = true)
public class TradeTopVolumeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "交易时间", name = "tradeDate", required = false)
    @TableField("trade_date")
    private Date tradeDate;

    @ApiModelProperty(value = "交易市场", name = "marketName", required = false)
    @TableField("market_name")
    private String marketName;


    @TableField("buy_user_id")
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Long buyUserId;

    @TableField("sell_user_id")
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Long sellUserId;


    @ApiModelProperty(value = "用户ID", name = "userId", required = false)
    @TableField(exist = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @ApiModelProperty(value = "交易量", name = "volume", example = "", required = false)
    @TableField("volume")
    private BigDecimal volume;

    @TableField("market_type")
    @ApiModelProperty(value = "市场类型：1 币币交易 2 创新交易", name = "marketType", required = false)
    private Integer marketType;

    @TableField("trade_type")
    @ApiModelProperty(value = "交易类型：1 买 2 卖", name = "tradeType", required = false)
    private Integer tradeType;

    public Long getUserId() {
        if (1 == this.getTradeType()) {//买则返回买方用户ID
            return this.getBuyUserId();
        }
        if (2 == this.getTradeType()) {//卖则返回买方用户ID
            return this.getSellUserId();
        }
        return null;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
