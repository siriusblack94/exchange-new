package com.blockeng.extend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 资金账户流水
 * </p>
 *
 * @author qiang
 * @since 2018-05-16
 */
@Data
@Accessors(chain = true)
@TableName("account_detail")
public class AccountDetail extends Model<AccountDetail> {

    private static final long serialVersionUID = 1L;

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
     * 账户id
     */
    @TableField("account_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long accountId;
    /**
     * 该笔流水资金关联方的账户id
     */
    @TableField("ref_account_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long refAccountId;
    /**
     * 订单ID
     */
    @TableField("order_id")
    private Long orderId;
    /**
     * 入账为1，出账为2
     */
    private Integer direction;
    /**
     * 业务类型:
     */
    @TableField("business_type")
    private String businessType;
    /**
     * 资产数量
     */
    private BigDecimal amount;

    /**
     * 手续费
     */
    private BigDecimal fee;

    /**
     * 流水状态：
     */
    private String remark;
    /**
     * 日期
     */
    private Date created;

    public AccountDetail() {
    }

    public AccountDetail(Long userId,
                         Long coinId,
                         Long accountId,
                         Long refAccountId,
                         Long orderId,
                         Integer direction,
                         String businessType,
                         BigDecimal amount,
                         String remark) {
        this.userId = userId;
        this.coinId = coinId;
        this.accountId = accountId;
        this.refAccountId = refAccountId;
        this.orderId = orderId;
        this.direction = direction;
        this.businessType = businessType;
        this.amount = amount;
        this.remark = remark;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
