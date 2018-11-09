package com.blockeng.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 数字货币充值
 * </p>
 *
 * @author crow
 * @since 2018-05-16
 */
@Data
@Accessors(chain = true)
@TableName("coin_recharge")
public class CoinRecharge extends Model<CoinRecharge> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户id
     */
    @TableField("user_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /**
     * 币种id
     */
    @TableField("coin_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long coinId;

    /**
     * 币种名称
     */
    @TableField("coin_name")
    private String coinName;

    /**
     * 币种类型
     */
    @TableField("coin_type")
    private String coinType;

    /**
     * 钱包地址
     */
    @TableField("address")
    private String address;

    /**
     * 状态
     * 0正常-1充值失败
     */
    @TableField("status")
    private Integer status;

    /**
     * 充值确认数
     */
    @TableField("confirm")
    private Integer confirm;

    /**
     * 交易id
     */
    @TableField("txid")
    private String txid;

    /**
     * 充值量
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 修改时间
     */
    @TableField("last_update_time")
    private Date lastUpdateTime;

    /**
     * 创建时间
     */
    @TableField("created")
    private Date created;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
