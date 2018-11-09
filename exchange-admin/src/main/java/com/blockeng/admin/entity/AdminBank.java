package com.blockeng.admin.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.Version;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 人民币充值卡号管理
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Data
@Accessors(chain = true)
@TableName("admin_bank")
@ApiModel(description = "人民币充值卡号管理")

public class AdminBank extends Model<AdminBank> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "主键id", name = "id", required = false)
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;
    /**
     * 开户人姓名
     */
    @ApiModelProperty(value = "开户人姓名", name = "name", required = true)

    private String name;
    /**
     * 开户行名称
     */
    @ApiModelProperty(value = "开户行名称", name = "bankName", required = true)

    @TableField("bank_name")
    private String bankName;
    /**
     * 卡号
     */
    @ApiModelProperty(value = "卡号", name = "bankCard", required = true)

    @TableField("bank_card")
    private String bankCard;
    /**
     * 充值转换换币种ID
     */
    @ApiModelProperty(value = "充值转换换币种ID", name = "coinId", required = true)

    @TableField("coin_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long coinId;
    /**
     * 币种名称
     */
    @ApiModelProperty(value = "币种名称", name = "coinName", required = true)

    @TableField("coin_name")
    private String coinName;
    /**
     * 状态：0-有效；1-无效；
     */
    @ApiModelProperty(value = "状态：0-有效；1-无效；", name = "status", required = true)

    private Integer status;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
