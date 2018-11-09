package com.blockeng.admin.entity;

import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;

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
 * 用户人民币提现地址
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Data
@Accessors(chain = true)
@TableName("user_bank")
@ApiModel(description = "用户人民币提现地址")
public class UserBank extends Model<UserBank> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @ApiModelProperty(value = "主键id", name = "id", required = false)
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", name = "userId", required = true)

    @TableField("user_id")
    private Long userId;

    /**
     * 银行卡名称
     */
    @ApiModelProperty(value = "银行卡名称", name = "remark", required = true)

    private String remark;
    /**
     * 开户人
     */
    @TableField("real_name")
    @ApiModelProperty(value = "开户人", name = "realName", required = true)

    private String realName;
    /**
     * 开户行
     */
    @ApiModelProperty(value = "开户行", name = "bank", required = true)

    private String bank;
    /**
     * 开户省
     */
    @TableField("bank_prov")
    @ApiModelProperty(value = "开户省", name = "bankProv", required = false)
    private String bankProv;
    /**
     * 开户市
     */
    @TableField("bank_city")
    @ApiModelProperty(value = "开户市", name = "bankProv", required = false)
    private String bankCity;
    /**
     * 开户地址
     */
    @ApiModelProperty(value = "开户地址", name = "bankAddr", required = false)
    @TableField("bank_addr")
    private String bankAddr;
    /**
     * 开户账号
     */
    @TableField("bank_card")
    @ApiModelProperty(value = "开户账号", name = "bankCard", required = true)
    private String bankCard;
    /**
     * 状态：0，禁用；1，启用；
     */
    @ApiModelProperty(value = "状态：0，禁用；1，启用；", name = "status", required = true)
    private Integer status;
    /**
     * 更新时间
     */
    @TableField("last_update_time")
    @ApiModelProperty(value = "更新时间", name = "lastUpdateTime", required = false)
    private Date lastUpdateTime;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", name = "created", required = false)
    private Date created;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
