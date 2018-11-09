package com.blockeng.boss.entity;

import lombok.Data;
import lombok.experimental.Accessors;

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
public class TurnoverOrder {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 市场ID
     */
    private Long marketId;
    /**
     * 市场标识符
     */
    private String symbol;
    /**
     * 市场名称
     */
    private String marketName;
    /**
     * 委托单类型：1-买；2-卖；
     */
    private Integer tradeType;
    /**
     * 卖方用户ID
     */
    private Long sellUserId;
    /**
     * 卖方币种ID
     */
    private Long sellCoinId;
    /**
     * 卖方委托订单ID
     */
    private Long sellOrderId;
    /**
     * 卖方委托价格
     */
    private BigDecimal sellPrice;
    /**
     * 卖方手续费率
     */
    private BigDecimal sellFeeRate;
    /**
     * 卖方委托数量
     */
    private BigDecimal sellVolume;
     /**
     * 买方用户ID
     */
    private Long buyUserId;

    /**
     * 买方币种ID
     */
    private Long BuyCoinId;

    /**
     * 买方委托订单ID
     */
    private Long buyOrderId;

    /**
     * 买方委托数量
     */
    private BigDecimal buyVolume;

    /**
     * 买方委托价格
     */
    private BigDecimal buyPrice;

    /**
     * 买方手续费率
     */
    private BigDecimal buyFeeRate;

    /**
     * 委托订单ID
     */
    private Long orderId;

    /**
     * 成交总额
     */
    private BigDecimal amount;

    /**
     * 成交价格
     */
    private BigDecimal price;

    /**
     * 成交数量
     */
    private BigDecimal volume;

    /**
     * 成交卖出手续费
     */
    private BigDecimal dealSellFee;

    /**
     * 成交卖出手续费率
     */
    private BigDecimal dealSellFeeRate;

    /**
     * 成交买入手续费
     */
    private BigDecimal dealBuyFee;

    /**
     * 成交买入成交率费
     */
    private BigDecimal dealBuyFeeRate;

    /**
     * 状态
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
}
