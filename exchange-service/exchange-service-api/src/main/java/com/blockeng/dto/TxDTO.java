package com.blockeng.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

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
@Document(collection = "tx")
public class TxDTO implements Serializable {

    private static final long serialVersionUID = -5057531952746023252L;

    /**
     * 交易对ID
     */
    private Long marketId;

    /**
     * 交易对标识符
     */
    private String symbol;

    /**
     * 交易对名称
     */
    private String marketName;

    /**
     * 成交类型（1-买；2-卖）
     */
    private int tradeType;

    /**
     * 买单订单号
     */
    private Long buyOrderId;

    /**
     * 买单币种ID
     */
    private Long buyCoinId;

    /**
     * 买单用户ID
     */
    private Long buyUserId;

    /**
     * 委托买单价格
     */
    private BigDecimal buyPrice;

    /**
     * 委托买单数量
     */
    private BigDecimal buyVolume;

    /**
     * 买单手续费费率
     */
    private BigDecimal buyFeeRate;

    /**
     * 委托卖单订单号
     */
    private Long sellOrderId;

    /**
     * 卖单币种ID
     */
    private Long sellCoinId;

    /**
     * 卖单用户ID
     */
    private Long sellUserId;

    /**
     * 委托卖单价格
     */
    private BigDecimal sellPrice;

    /**
     * 委托卖单数量
     */
    private BigDecimal sellVolume;

    /**
     * 卖单手续费费率
     */
    private BigDecimal sellFeeRate;

    /**
     * 成交量
     */
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
    private Date created;
}
