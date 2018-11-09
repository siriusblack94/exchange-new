package com.blockeng.admin.entity;

import com.baomidou.mybatisplus.enums.IdType;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.Version;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 成交数据
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Data
@Accessors(chain = true)
@TableName("turnover_record")
public class TurnoverRecord extends Model<TurnoverRecord> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 市场ID
     */
    @TableField("market_id")
    private Long marketId;
    /**
     * 成交价
     */
    @TableField("trade_price")
    private BigDecimal tradePrice;
    /**
     * 成交数量
     */
    @TableField("trade_num")
    private BigDecimal tradeNum;
    /**
     * 创建时间
     */
    private Date created;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
