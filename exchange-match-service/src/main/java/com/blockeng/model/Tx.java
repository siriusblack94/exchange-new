package com.blockeng.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@Document(collection = "tx")
public class Tx implements Serializable {

    private static final long serialVersionUID = -6388382264433675142L;

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
     * 交易对类型
     */
    private int marketType;

    /**
     * 成交类型（1-买；2-卖 ；3取消单）
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
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created;


    /**
     * 撤单标志
     */
    private boolean isCancel = false;
}
