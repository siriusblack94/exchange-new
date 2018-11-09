package com.blockeng.boss.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/6/17 下午12:27
 * @Modified by: Chen Long
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@Document(collection = "turnover_order")
public class DealOrder implements Serializable {

    private static final long serialVersionUID = -5057531952746023252L;

    @Id
    private String objectId;

    /**
     * 交易对ID
     */
    @Field("market_id")
    private Long marketId;

    /**
     * 交易对标识符
     */
    @Indexed
    private String symbol;

    /**
     * 交易对名称
     */
    @Field("market_name")
    private String marketName;

    /**
     * 交易对类型
     */
    @Field("market_type")
    private int marketType;

    /**
     * 成交类型（1-买；2-卖）
     */
    @Field("trade_type")
    private int tradeType;

    /**
     * 买单订单号
     */
    @Field("buy_order_id")
    private Long buyOrderId;

    /**
     * 买单币种ID
     */
    @Field("buy_coin_id")
    private Long buyCoinId;

    /**
     * 买单用户ID
     */
    @Field("buy_user_id")
    private Long buyUserId;

    /**
     * 委托买单价格
     */
    @Field("buy_price")
    private BigDecimal buyPrice;

    /**
     * 委托买单数量
     */
    @Field("buy_volume")
    private BigDecimal buyVolume;

    /**
     * 买单手续费费率
     */
    @Field("buy_fee_rate")
    private BigDecimal buyFeeRate;

    /**
     * 委托卖单订单号
     */
    @Field("sell_order_id")
    private Long sellOrderId;

    /**
     * 卖单币种ID
     */
    @Field("sell_coin_id")
    private Long sellCoinId;

    /**
     * 卖单用户ID
     */
    @Field("sell_user_id")
    private Long sellUserId;

    /**
     * 委托卖单价格
     */
    @Field("sell_price")
    private BigDecimal sellPrice;

    /**
     * 委托卖单数量
     */
    @Field("sell_volume")
    private BigDecimal sellVolume;

    /**
     * 卖单手续费费率
     */
    @Field("sell_fee_rate")
    private BigDecimal sellFeeRate;

    /**
     * 成交量
     */
    @Field("volume")
    private BigDecimal vol;

    /**
     *
     */
    private BigDecimal amount;

    /**
     * 成交价格
     */
    private BigDecimal price;

    /**
     * 创建时间
     */
    @Indexed
    private Date created;

    private boolean isCancel;
}
