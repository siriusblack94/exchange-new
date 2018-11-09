package com.blockeng.trade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 交易对配置信息
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Data
@Accessors(chain = true)
public class Market extends Model<Market> {

    private static final long serialVersionUID = 1L;

    /**
     * 市场ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 交易区域ID
     */
    @TableField("trade_area_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long tradeAreaId;
    /**
     * 卖方市场ID
     */
    @TableField("sell_coin_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long sellCoinId;
    /**
     * 买方币种ID
     */
    @TableField("buy_coin_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long buyCoinId;
    /**
     * 交易对标识
     */
    private String symbol;
    /**
     * 名称
     */
    private String name;
    /**
     * 标题
     */
    private String title;
    /**
     * 市场logo
     */
    private String img;
    /**
     * 开盘价格
     */
    @TableField("open_price")
    private BigDecimal openPrice;

    /**
     * 买入手续费率
     */
    @TableField("fee_buy")
    private BigDecimal feeBuy;

    /**
     * 卖出手续费率
     */
    @TableField("fee_sell")
    private BigDecimal feeSell;

    /**
     * 单笔最小委托量
     */
    @TableField("num_min")
    private BigDecimal numMin;

    /**
     * 单笔最大委托量
     */
    @TableField("num_max")
    private BigDecimal numMax;

    /**
     * 单笔最小成交额
     */
    @TableField("trade_min")
    private BigDecimal tradeMin;

    /**
     * 单笔最大成交额
     */
    @TableField("trade_max")
    private BigDecimal tradeMax;

    /**
     * 价格小数位
     */
    @TableField("price_scale")
    private Integer priceScale;

    /**
     * 数量小数位
     */
    @TableField("num_scale")
    private Integer numScale;

    /**
     * 合并深度(格式：0.0001,0.001,0.01)
     */
    @TableField("merge_depth")
    private String mergeDepth;

    /**
     * 交易时间
     */
    @TableField("trade_time")
    private String tradeTime;

    /**
     * 交易周期
     */
    @TableField("trade_week")
    private String tradeWeek;

    /**
     * 排序列
     */
    private Integer sort;

    /**
     * 状态：0-禁用；1-启用；
     */
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
