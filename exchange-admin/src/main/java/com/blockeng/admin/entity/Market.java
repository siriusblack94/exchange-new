package com.blockeng.admin.entity;

import com.baomidou.mybatisplus.enums.IdType;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.Version;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 交易对配置信息
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Data
@Accessors(chain = true)
public class Market extends Model<Market> {

    private static final long serialVersionUID = 1L;

    /**
     * 市场ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;
    /**
     * 类型：1-数字货币；2：创新交易
     */
    @ApiModelProperty(value = "类型", name = "type", example = "1 币币交易 2 创新交易", required = true)
    private Integer type;
    /**
     * 交易区域ID
     */
    @TableField("trade_area_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long tradeAreaId;
    /**
     * 卖方币种ID
     */
    @TableField("sell_coin_id")
    @ApiModelProperty(value = "基础货币ID", name = "sellCoinid", example = "", required = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long sellCoinid;
    /**
     * 买方币种ID
     */
    @TableField("buy_coin_id")
    @ApiModelProperty(value = "报价货币ID", name = "buyCoinid", example = "", required = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long buyCoinid;
    /**
     * 名称
     */
    @ApiModelProperty(value = "市场名称", name = "name", example = "", required = true)
    private String name;

    /**
     * symbol
     */
    @ApiModelProperty(value = "市场标志符", name = "symbol", example = "", required = true)
    private String symbol;
    /**
     * 标题
     */
    @ApiModelProperty(value = "市场标题", name = "title", example = "", required = true)
    private String title;
    /**
     * 市场logo
     */
    @ApiModelProperty(value = "市场logo地址", name = "img", example = "", required = false)
    private String img;
    /**
     * 开盘价格
     */
    @TableField("open_price")
    @ApiModelProperty(value = "开盘价", name = "openPrice", example = "", required = true)
    private BigDecimal openPrice;
    /**
     * 买入手续费率
     */
    @TableField("fee_buy")
    @ApiModelProperty(value = "买入手续费率", name = "feeBuy", example = "", required = true)
    private BigDecimal feeBuy;
    /**
     * 卖出手续费率
     */
    @TableField("fee_sell")
    @ApiModelProperty(value = "卖出手续费率", name = "feeSell", example = "", required = true)
    private BigDecimal feeSell;
    /**
     * 保证金占用比例
     */
    @TableField("margin_rate")
    @ApiModelProperty(value = "保证金占用比例", name = "marginRate", example = "", required = false)
    private BigDecimal marginRate;
    /**
     * 单笔最小委托量
     */
    @TableField("num_min")
    @ApiModelProperty(value = "最小委托量", name = "numMin", example = "", required = true)
    private BigDecimal numMin;
    /**
     * 单笔最大委托量
     */
    @TableField("num_max")
    @ApiModelProperty(value = "最大委托量", name = "numMax", example = "", required = true)
    private BigDecimal numMax;
    /**
     * 单笔最小成交额
     */
    @TableField("trade_min")
    @ApiModelProperty(value = "最小成交额", name = "tradeMin", example = "", required = false)
    private BigDecimal tradeMin;
    /**
     * 单笔最大成交额
     */
    @TableField("trade_max")
    @ApiModelProperty(value = "最大成交额", name = "tradeMax", example = "", required = false)
    private BigDecimal tradeMax;
    /**
     * 价格小数位
     */
    @TableField("price_scale")
    @ApiModelProperty(value = "价格小数位", name = "priceScale", example = "", required = true)
    private Integer priceScale;
    /**
     * 数量小数位
     */
    @TableField("num_scale")
    @ApiModelProperty(value = "数量小数位", name = "numScale", example = "", required = true)
    private Integer numScale;
    /**
     * 合并深度(格式：0.0001,0.001,0.01)
     */
    @TableField("merge_depth")
    @ApiModelProperty(value = "合并深度", name = "mergeDepth", example = "0.0001,0.001,0.01", required = false)
    private String mergeDepth;
    /**
     * 合约单位
     */
    @TableField("contract_unit")
    @ApiModelProperty(value = "合约单位", name = "contractUnit", example = "", required = false)
    private Integer contractUnit;
    /**
     * 点
     */
    @TableField("point_value")
    @ApiModelProperty(value = "点", name = "pointValue", example = "", required = false)
    private BigDecimal pointValue;
    /**
     * 交易时间
     */
    @TableField("trade_time")
    @ApiModelProperty(value = "交易时间", name = "tradeTime", example = "", required = true)
    private String tradeTime;
    /**
     * 交易周期
     */
    @TableField("trade_week")
    @ApiModelProperty(value = "交易周期", name = "tradeWeek", example = "", required = true)
    private String tradeWeek;
    /**
     * 排序列
     */
    @ApiModelProperty(value = "排序", name = "sort", example = "", required = true)
    private Integer sort;
    /**
     * 状态
     * 0禁用
     * 1启用
     */
    @ApiModelProperty(value = "状态", name = "status", example = "0 禁用 1 启用", required = true)
    private Integer status;
    /**
     * 福汇API交易对
     */
    @TableField("fxcm_symbol")
    @ApiModelProperty(value = "福汇API交易对", name = "fxcmSymbol", example = "", required = false)
    private String fxcmSymbol;
    /**
     * 对应雅虎金融API交易对
     */
    @TableField("yahoo_symbol")
    @ApiModelProperty(value = "雅虎API交易对", name = "yahooSymbol", example = "", required = false)
    private String yahooSymbol;
    /**
     * 对应阿里云API交易对
     */
    @TableField("aliyun_symbol")
    @ApiModelProperty(value = "阿里云API交易对", name = "aliyunSymbol", example = "", required = false)
    private String aliyunSymbol;
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
