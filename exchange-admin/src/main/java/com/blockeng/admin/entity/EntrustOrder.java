package com.blockeng.admin.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.experimental.Accessors;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 委托订单信息
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Data
@Accessors(chain = true)
@TableName("entrust_order")
public class EntrustOrder extends Model<EntrustOrder> {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    @ApiModelProperty(value = "用户ID", name = "userId", example = "", required = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /**
     * 市场ID
     */
    @TableField("market_id")
    @ApiModelProperty(value = "市场ID", name = "marketId", example = "", required = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long marketId;

    /**
     * 市场类型（1：币币交易，2：创新交易）
     */
    @TableField("market_type")
    @ApiModelProperty(value = "市场类型", name = "marketType", example = "1 币币交易，2 创新交易", required = false)
    private Integer marketType;

    /**
     * 市场标识符
     */
    @TableField("symbol")
    @ApiModelProperty(value = "市场标识符", name = "symbol", example = "", required = false)
    private String symbol;

    /**
     * 交易市场
     */
    @TableField("market_name")
    @ApiModelProperty(value = "交易市场名称", name = "marketName", example = "", required = false)
    private String marketName;

    /**
     * 委托价格
     */
    @TableField("price")
    @ApiModelProperty(value = "委托价格", name = "price", example = "", required = false)
    private BigDecimal price;

    /**
     * 合并深度价格1
     */
    @TableField("merge_low_price")
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private BigDecimal mergeLowPrice;

    /**
     * 合并深度价格2
     */
    @TableField("merge_high_price")
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private BigDecimal mergeHighPrice;

    /**
     * 委托数量
     */
    @ApiModelProperty(value = "委托数量", name = "volume", example = "", required = false)
    private BigDecimal volume;


    /**
     * 预计成交额
     */
    @ApiModelProperty(value = "预计成交额", name = "price", example = "", required = false)
    @TableField(exist = false)
    private BigDecimal predictTurnoverAmount;

    /**
     * 委托总额
     */
    @ApiModelProperty(value = "委托总额", name = "amount", example = "", required = false)
    private BigDecimal amount;

    /**
     * 手续费比率
     */
    @TableField("fee_rate")
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private BigDecimal feeRate;

    /**
     * 交易手续费
     */
    @ApiModelProperty(value = "手续费", name = "fee", example = "", required = false)
    private BigDecimal fee;

    /**
     * 合约单位
     */
    @TableField("contract_unit")
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Integer contractUnit;

    /**
     * 成交数量
     */
    @ApiModelProperty(value = "成交量", name = "deal", example = "", required = false)
    private BigDecimal deal;

    /**
     * 冻结量
     */
    @ApiModelProperty(value = "冻结资金", name = "freeze", example = "", required = false)
    private BigDecimal freeze;

    /**
     * 占用保证金
     */
    @TableField("lock_margin")
    @ApiModelProperty(value = "占用保证金", name = "freeze", example = "", required = false)
    private BigDecimal lockMargin;

    /**
     * 价格类型：1-市价；2-限价
     */
    @TableField("price_type")
    @ApiModelProperty(value = "价格类型", name = "priceType", example = "1 市价 2 限价", required = false)
    private Integer priceType;

    /**
     * 交易类型：1-开仓；2-平仓
     */
    @TableField("trade_type")
    @ApiModelProperty(value = "交易类型", name = "tradeType", example = "1 开仓 2 平仓", required = false)
    private Integer tradeType;

    /**
     * 买卖类型：1-买入；2-卖出
     */
    @ApiModelProperty(value = "交易方式", name = "type", example = "1-买入；2-卖出", required = false)
    private Integer type;

    /**
     * 平仓订单号
     */
    @TableField("open_order_id")
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Long openOrderId;

    /**
     * status 0未成交 1已成交 2已取消 4异常单
     */
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
    @ApiModelProperty(value = "委托时间", name = "created", example = "", required = false)
    private Date created;

    /**
     * 用户名
     */
    @TableField(value = "userName", exist = false)
    private String userName;

    /**
     * 用户手机号
     */
    @TableField(value = "mobile", exist = false)
    private String mobile;

    /**
     * 交易方式字符串(for导出)
     * 1.买 2.卖
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String typeStr;

    /**
     * 价格类型字符串(for导出)
     * 1-市价；2-限价
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String priceTypeStr;

    /**
     * 订单类型字符串(for导出)
     * 1-开仓；2-平仓
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String tradeTypeStr;

    /**
     * 订单状态字符串(for导出)
     * 0未成交 1已成交 2已取消 4异常单
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String statusStr;

    /**
     * 用户ID字符串(for导出)
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String userIdStr;

    /**
     * ID字符串(for导出)
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String idStr;

    /**
     * 创建时间字符串(for导出)
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String createdStr;

    /**
     * 已成交量字符串(for导出)
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String dealStr;

    /**
     * 冻结金额字符串(for导出)
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String freezeStr;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    /**
     * 预计成交额= 成交量 * 成交价格 + 手续费
     *
     * @return
     */
//    public BigDecimal getPredictTurnoverAmount() {
//        return this.getFee().add(
//                this.getPrice().multiply(this.getVolume()))
//                .setScale(8, BigDecimal.ROUND_HALF_UP);
//    }

    public void setPredictTurnoverAmount(BigDecimal predictTurnoverAmount) {
        this.predictTurnoverAmount = predictTurnoverAmount;
    }

    public String getTypeStr() {
        if (null == this.getType()) {
            return "";
        }
        if (1 == this.getType()) {
            return "买入";
        }
        if (2 == this.getType()) {
            return "卖出";
        }
        return "";
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    public String getPriceTypeStr() {
        if (null == this.getPriceType()) {
            return "";
        }
        if (1 == this.getPriceType()) {
            return "市价";
        }
        if (2 == this.getPriceType()) {
            return "限价";
        }
        return "";
    }

    public void setPriceTypeStr(String priceTypeStr) {
        this.priceTypeStr = priceTypeStr;
    }

    public String getStatusStr() {
        if (null == this.getStatus()) {
            return "";
        }
        if (0 == this.getStatus()) {
            return "未成交";
        }
        if (1 == this.getStatus()) {
            return "已成交";
        }
        if (2 == this.getStatus()) {
            return "已取消";
        }
        if (4 == this.getStatus()) {
            return "异常单";
        }
        return "";
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public String getTradeTypeStr() {
        if (null == this.getTradeType()) {
            return "";
        }
        if (1 == this.getTradeType()) {
            return "开仓";
        }
        if (2 == this.getTradeType()) {
            return "平仓";
        }
        return "";
    }

    public void setTradeTypeStr(String tradeTypeStr) {
        this.tradeTypeStr = tradeTypeStr;
    }

    public String getUserIdStr() {
        if (null == this.getUserId()) {
            return "";
        }
        //导出ID过长
        return "\t" + String.valueOf(this.getUserId());
    }

    public void setUserIdStr(String userIdStr) {
        this.userIdStr = userIdStr;
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

    public String getDealStr() {
        if (0 == this.getDeal().compareTo(BigDecimal.ZERO)) {
            return " 0 ";
        }
        return "\t" + String.valueOf(this.getDeal());
    }

    public void setDealStr(String dealStr) {
        this.dealStr = dealStr;
    }

    public String getFreezeStr() {
        if (0 == this.getFreeze().compareTo(BigDecimal.ZERO)) {
            return " 0 ";
        }
        return "\t" + String.valueOf(this.getFreeze());
    }

    public void setFreezeStr(String freezeStr) {
        this.freezeStr = freezeStr;
    }
}
