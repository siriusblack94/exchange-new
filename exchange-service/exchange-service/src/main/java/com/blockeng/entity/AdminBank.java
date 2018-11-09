package com.blockeng.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.Version;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 人民币充值卡号管理
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Data
@Accessors(chain = true)
@TableName("admin_bank")
public class AdminBank extends Model<AdminBank> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 开户人姓名
     */
    private String name;
    /**
     * 开户行名称
     */
    @TableField("bank_name")
    private String bankName;
    /**
     * 卡号
     */
    @TableField("bank_card")
    private String bankCard;
    /**
     * 充值转换换币种ID
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
     * 状态：1-有效；2-无效；
     */
    private Integer status;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
