package com.blockeng.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
@Document(collection = "entrust_order")
public class EntrustOrder {

    /**
     * 订单ID
     */
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户ID
     */
    @Field("user_id")
    @TextIndexed
    private Long userId;

    /**
     * 市场ID
     */
    @Field("market_id")
    private Long marketId;

    /**
     * 市场标识符
     */
    @Field("symbol")
    @TextIndexed
    private String symbol;

    /**
     * 交易市场
     */
    @Field("market_name")
    private String marketName;

    /**
     * 委托价格
     */
    @TextIndexed
    private BigDecimal price;

    /**
     * 委托数量
     */
    @TextIndexed
    private BigDecimal volume;

    /**
     * 委托总额
     */
    private BigDecimal amount;

    /**
     * 手续费比率
     */
    @Field("fee_rate")
    private BigDecimal feeRate;

    /**
     * 交易手续费
     */
    private BigDecimal fee;

    /**
     * 成交数量
     */
    private BigDecimal deal;

    /**
     * 冻结量
     */
    private BigDecimal freeze;

    /**
     * 买卖类型：1-买入；2-卖出
     */
    private Integer type;

    /**
     * status
     */
    private Integer status;

    /**
     * 更新时间
     */
    @Field("last_update_time")
    private Date lastUpdateTime;

    /**
     * 创建时间
     */
    private Date created;
}
