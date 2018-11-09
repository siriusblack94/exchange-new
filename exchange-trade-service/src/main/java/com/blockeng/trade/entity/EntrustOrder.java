package com.blockeng.trade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
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
@TableName("entrust_order")
@Document(collection = "entrust_order")
public class EntrustOrder extends Model<EntrustOrder> {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    @Field("user_id")
    private Long userId;

    /**
     * 市场ID
     */
    @TableField("market_id")
    @Field("market_id")
    private Long marketId;

    /**
     * 市场标识符
     */
    @TableField("symbol")
    @Field("symbol")
    private String symbol;

    /**
     * 交易市场
     */
    @TableField("market_name")
    @Field("market_name")
    private String marketName;

    /**
     * 委托价格
     */
    @TableField("price")
    private BigDecimal price;

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
    @TableField("fee_rate")
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
    @TableField("last_update_time")
    @Field("last_update_time")
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
