package com.blockeng.boss.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author qiang
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@Document(collection = "events")
public class EntrustOrderMatchDTO implements Serializable {

    private static final long serialVersionUID = 2001733705944713209L;

    /**
     * 订单ID
     */
    @Id
    private Long id;

    /**
     * 用户ID
     */
    @Field("user_id")
    private Long userId;

    /**
     * 交易对ID
     */
    @Field("markdet_id")
    private Long marketId;

    /**
     * 交易对标识符
     */

    private String symbol;

    /**
     * 交易对名称
     */
    @Field("markdet_name")
    private String marketName;

    /**
     * 交易对类型
     */
    @Field("markdet_type")
    private int marketType;

    /**
     * 币种ID
     */
    @Field("coin_id")
    private Long coinId;

    /**
     * 委托价格
     */
    private BigDecimal price;

    /**
     * 合并深度价格1
     */
    @Field("merge_low_price")
    private BigDecimal mergeLowPrice;

    /**
     * 合并深度价格2
     */
    @Field("merge_high_price")
    private BigDecimal mergeHighPrice;

    /**
     * 手续费费率
     */
    @Field("fee_rate")
    private BigDecimal feeRate;

    /**
     * 委托数量
     */
    private BigDecimal volume;

    /**
     * 已成交
     */
    private BigDecimal deal;

    /**
     * 买卖类型：1-买入；2-卖出
     */
    private int type;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 状态
     */
    private int status;

    /**
     * 冻结量
     */
    private BigDecimal freeze;
}
