package com.blockeng.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jopenexchg.matcher.Market;
import org.jopenexchg.matcher.OrderType;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * 交易订单
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@Document(collection = "entrust_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 9179887994734199109L;

    /**
     * 订单ID
     */
    private Long id;

    /**
     * 用户ID
     */
    @Field("user_id")
    private Long userId;

    /**
     * 交易对ID
     */
    @Field("market_id")
    private Long marketId;

    /**
     * 交易对标识符
     */
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
     * 冻结量
     */
    private BigDecimal freeze;

    /**
     * 买卖类型：1-买入；2-卖出
     */
    private Integer type;

    public OrderType getSide() {
        return type == 1 ? OrderType.BUY : OrderType.SELL;
    }

    private OrderType side;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created;

    public BigDecimal getOver() {
        return volume.subtract(deal);
    }

    /**
     * 剩余数量
     */
    private BigDecimal over;

    /**
     * 订单删除标志，撤单时不用立刻从列表里面摘除
     */
    private boolean delflg = false;

    /**
     * CONTEXT 区域，采用 Lazy Loading 方式
     * lazy loading, 保证一个订单最多只查询一次持仓
     * 持仓库的查询和更新速度是很快的: 根据基准测试，更新是335W/s, 查询是709W/s
     * 对于证券的卖出方，应该在前端检查的时候就检查余额同时设置此字段
     * 对于证券的买入方，则是在发生撮合匹配的时候才需要查询并同时设置此字段
     */
    private Market market = null;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}