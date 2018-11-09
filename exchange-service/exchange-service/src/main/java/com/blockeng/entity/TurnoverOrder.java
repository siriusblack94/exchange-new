package com.blockeng.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 成交订单
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Data
@Accessors(chain = true)
@TableName("turnover_order")
public class TurnoverOrder extends Model<TurnoverOrder> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 市场ID
     */
    @TableField("market_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long marketId;

    /**
     * 市场标识符
     */
    @TableField("symbol")
    private String symbol;

    /**
     * 市场名称
     */
    @TableField("market_name")
    private String marketName;

    /**
     * 委托单类型：1-买；2-卖；
     */
    @TableField("trade_type")
    private Integer tradeType;

    /**
     * 卖方用户ID
     */
    @TableField("sell_user_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long sellUserId;

    /**
     * 卖方币种ID
     */
    @TableField("sell_coin_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long sellCoinId;

    /**
     * 卖方委托订单ID
     */
    @TableField("sell_order_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long sellOrderId;

    /**
     * 卖方委托价格
     */
    @TableField("sell_price")
    private BigDecimal sellPrice;

    /**
     * 卖方手续费率
     */
    @TableField("sell_fee_rate")
    private BigDecimal sellFeeRate;

    /**
     * 卖方委托数量
     */
    @TableField("sell_volume")
    private BigDecimal sellVolume;

    /**
     * 买方用户ID
     */
    @TableField("buy_user_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long buyUserId;

    /**
     * 买方币种ID
     */
    @TableField("buy_coin_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long BuyCoinId;

    /**
     * 买方委托订单ID
     */
    @TableField("buy_order_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long buyOrderId;

    /**
     * 买方委托数量
     */
    @TableField("buy_volume")
    private BigDecimal buyVolume;

    /**
     * 买方委托价格
     */
    @TableField("buy_price")
    private BigDecimal buyPrice;

    /**
     * 买方手续费率
     */
    @TableField("buy_fee_rate")
    private BigDecimal buyFeeRate;

    /**
     * 委托订单ID
     */
    @TableField("order_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orderId;

    /**
     * 成交总额
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 成交价格
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 成交数量
     */
    @TableField("volume")
    private BigDecimal volume;

    /**
     * 成交卖出手续费
     */
    @TableField("deal_sell_fee")
    private BigDecimal dealSellFee;

    /**
     * 成交卖出手续费率
     */
    @TableField("deal_sell_fee_rate")
    private BigDecimal dealSellFeeRate;

    /**
     * 成交买入手续费
     */
    @TableField("deal_buy_fee")
    private BigDecimal dealBuyFee;

    /**
     * 成交买入成交率费
     */
    @TableField("deal_buy_fee_rate")
    private BigDecimal dealBuyFeeRate;

    @TableField("status")
    private Integer status;

    /**
     * 更新时间
     */
    @TableField("last_update_time")
    private Date lastUpdateTime;

    /**
     * 创建时间
     */
    private Date created;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
