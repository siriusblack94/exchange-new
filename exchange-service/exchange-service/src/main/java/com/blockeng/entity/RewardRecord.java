package com.blockeng.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 奖励记录
 * @author shadow
 * @created 2018/10/19
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName(value = "reward_record")
public class RewardRecord extends Model<RewardRecord> {


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
     * 业务类型:
     */
    @TableField("business_type")
    private String businessType;
    /**
     * 资产数量
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
    /**
     * 日期
     */
    @TableField("created")
    private Date created;


    public RewardRecord(Long userId,
                         Long coinId,
                         Long accountId,
                         String businessType,
                         BigDecimal amount,
                         String remark) {
        this.userId = userId;
        this.coinId = coinId;
        this.accountId = accountId;
        this.businessType = businessType;
        this.amount = amount;
        this.remark = remark;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
