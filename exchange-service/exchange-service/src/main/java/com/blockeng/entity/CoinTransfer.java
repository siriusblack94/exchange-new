package com.blockeng.entity;

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
 * @Author: jakiro
 * @Date: 2018-10-30 14:32
 * @Description: 站内转帐表实体
 */
@Data
@Accessors(chain = true)
@TableName("coin_transfer")
public class CoinTransfer extends Model<CoinTransfer> {

    private static final long serialVersionUID = 1L;
    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 币种ID
     */
    @TableField(value = "coin_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long coinId;

    /**
     * 打款人用户ID
     */
    @TableField(value = "money_maker_user_id")
    private Long moneyMakerUserId;

    /**
     * 收款人ID
     */
    @TableField(value = "payee_user_id")
    private Long payeeUserId;

    /**
     * 打款数量
     */
    @TableField(value = "num")
    private BigDecimal num;

    /**
     * 状态 0:失败 1:成功 2:异常
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "created")
    private Date created;

    /**
     * 更新时间
     * */
    @TableField(value = "last_update_time")
    private Date lastUpdateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
