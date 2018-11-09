package com.blockeng.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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
@AllArgsConstructor
@NoArgsConstructor
public class EntrustOrderDTO {

    /**
     * 订单ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /**
     * 市场ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long marketId;

    /**
     * 市场类型（1：币币交易，2：创新交易）
     */
    private Integer marketType;

    /**
     * 市场标识符
     */
    private String symbol;

    /**
     * 交易市场
     */
    private String marketName;

    /**
     * 委托价格
     */
    private BigDecimal price;

    /**
     * 合并深度价格1
     */
    private BigDecimal mergeLowPrice;

    /**
     * 合并深度价格2
     */
    private BigDecimal mergeHighPrice;

    /**
     * 委托数量
     */
    private BigDecimal volume;

    /**
     * 委托总额
     */
    private BigDecimal amount;

    /**
     * 手续费比率
     */
    private BigDecimal feeRate;

    /**
     * 交易手续费
     */
    private BigDecimal fee;

    /**
     * 合约单位
     */
    private Integer contractUnit;

    /**
     * 成交数量
     */
    private BigDecimal deal;

    /**
     * 冻结量
     */
    private BigDecimal freeze;

    /**
     * 占用保证金
     */
    private BigDecimal lockMargin;

    /**
     * 价格类型：1-市价；2-限价
     */
    private Integer priceType;

    /**
     * 交易类型：1-开仓；2-平仓
     */
    private Integer tradeType;

    /**
     * 买卖类型：1-买入；2-卖出
     * 2 卖出
     */
    private Integer type;

    /**
     * status
     * 0未成交
     * 1已成交
     * 2已取消
     * 4异常单
     */
    private Integer status;

    /**
     * 更新时间
     */
    private Date lastUpdateTime;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * priceType 与 tradeType 合并
     */
    private String typeId;

    /**
     * 交易用户名
     */
    private String userName;

    /**
     * typeId 中文描述
     */
    private String typeDesc;

    /**
     * 触发价位
     */
    private BigDecimal triggerPrice;
    /**
     * 触发价位距离
     */
    private BigDecimal priceRange;

    /**
     * 当前最新成交价格
     */
    private BigDecimal currentPrice;

    /**
     * 上级
     */
    private String parentName;

    /**
     * 成交均价
     */
    private BigDecimal avgPrice;
}
