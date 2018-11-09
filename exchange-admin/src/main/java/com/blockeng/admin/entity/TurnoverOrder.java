package com.blockeng.admin.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.joda.time.DateTime;

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
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 市场ID
     */
    @TableField("market_id")
    @ApiModelProperty(value = "市场ID", name = "marketId", example = "", required = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long marketId;

    /**
     * 市场类型：1-币币交易；2-创新交易；
     */
    @TableField("market_type")
    private Integer marketType;

    /**
     * 交易类型：1 买 2 卖
     */
    @TableField("trade_type")
    @ApiModelProperty(value = "交易方式", name = "tradeType", example = "1 买 2 卖", required = false)
    private Integer tradeType;

    /**
     * 市场标识符
     */
    @TableField("symbol")
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String symbol;


    /**
     * 卖方用户ID
     */
    @TableField("sell_user_id")
    @ApiModelProperty(value = "卖方用户ID", name = "sellUserId", example = "", required = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long sellUserId;

    /**
     * 卖方币种ID
     */
    @TableField("sell_coin_id")
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Long sellCoinId;

    /**
     * 卖方委托订单ID
     */
    @TableField("sell_order_id")
    @ApiModelProperty(value = "卖方委托订单ID", name = "sellOrderId", example = "", required = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long sellOrderId;

    /**
     * 卖方委托价格
     */
    @TableField("sell_price")
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private BigDecimal sellPrice;

    /**
     * 卖方手续费率
     */
    @TableField("sell_fee_rate")
    @ApiModelProperty(value = "卖方手续", name = "sellFeeRate", example = "", required = false)
    private BigDecimal sellFeeRate;

    /**
     * 卖方委托数量
     */
    @TableField("sell_volume")
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private BigDecimal sellVolume;

    /**
     * 买方用户ID
     */
    @TableField("buy_user_id")
    @ApiModelProperty(value = "买方用户ID", name = "buyUserId", example = "", required = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long buyUserId;

    /**
     * 买方币种ID
     */
    @TableField("buy_coin_id")
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Long BuyCoinId;

    /**
     * 买方委托订单ID
     */
    @TableField("buy_order_id")
    @ApiModelProperty(value = "买方委托订单ID", name = "buyOrderId", example = "", required = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long buyOrderId;

    /**
     * 买方委托数量
     */
    @TableField("buy_volume")
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private BigDecimal buyVolume;

    /**
     * 买方委托价格
     */
    @TableField("buy_price")
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private BigDecimal buy_price;

    /**
     * 买方手续费率
     */
    @TableField("buy_fee_rate")
    @ApiModelProperty(value = "买方手续费", name = "buyFeeRate", example = "", required = false)
    private BigDecimal buyFeeRate;

    /**
     * 委托订单ID
     */
    @TableField("order_id")
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Long orderId;

    /**
     * 成交总额
     */
    @TableField("amount")
    @ApiModelProperty(value = "成交额", name = "amount", example = "", required = false)
    private BigDecimal amount;

    /**
     * 成交价格
     */
    @TableField("price")
    @ApiModelProperty(value = "成交价格", name = "price", example = "", required = false)
    private BigDecimal price;

    /**
     * 成交数量
     */
    @TableField("volume")
    @ApiModelProperty(value = "成交量", name = "volume", example = "", required = false)
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
    @JsonIgnore
    @ApiModelProperty(hidden = true)
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
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private BigDecimal dealBuyFeeRate;

    /**
     * 状态
     * 0未成交 1已成交 2已取消 4异常单
     */
    @TableField("status")
    @ApiModelProperty(value = "状态", name = "status", example = "0未成交 1已成交 2已取消 4异常单", required = false)
    private Integer status;

    /**
     * 更新时间
     */
    @TableField("last_update_time")
    private Date lastUpdateTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "成交时间", name = "created", example = "", required = false)
    private Date created;


    /**
     * 买方用户名
     */
    @TableField(value = "buy_user_name", exist = false)
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String buyUserName;

    /**
     * 卖方用户名
     */
    @TableField(value = "sell_user_name", exist = false)
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String sellUserName;

    /**
     * 市场名称
     */
    @TableField(value = "market_name", exist = false)
    @ApiModelProperty(value = "市场名称", name = "marketName", example = "", required = false)
    private String marketName;

    /**
     * ID字符串(for导出)
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String idStr;

    /**
     * 交易类型字符串(for导出)
     * 1 买 2 卖
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String tradeTypeStr;

    /**
     * 创建时间字符串(for导出)
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String createdStr;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public String getIdStr() {
        if (null == this.getId()) {
            return "";
        }
        //导出ID过长
        return "\t" + String.valueOf(this.getId());
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public String getTradeTypeStr() {
        if (null == this.getTradeType()) {
            return "";
        }
        if (1 == this.getTradeType()) {
            return "买";
        }
        if (2 == this.getTradeType()) {
            return "卖";
        }
        if(3 == this.getTradeType()){
            return "自交易";
        }
        return "";
    }

    public void setTradeTypeStr(String tradeTypeStr) {
        this.tradeTypeStr = tradeTypeStr;
    }

    public String getCreatedStr() {
        if (null == this.getCreated()) {
            return "";
        }
        DateTime dateTime = new DateTime(this.getCreated());
        return dateTime.toString("yyyy-MM-dd HH:mm:ss");
    }

    public void setCreatedStr(String createdStr) {
        this.createdStr = createdStr;
    }
}
