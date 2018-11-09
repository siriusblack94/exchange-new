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
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户钱包表
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Data
@Accessors(chain = true)
@TableName("user_wallet")
public class UserWallet extends Model<UserWallet> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    @ApiModelProperty(value = "主键id", name = "id", required = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 用户ID
     */
    @TableField("user_id")
    @ApiModelProperty(value = "用户ID", name = "userId", required = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    /**
     * 币种ID
     */
    @TableField("coin_id")
    @ApiModelProperty(value = "币种ID", name = "coinId", required = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long coinId;
    /**
     * 币种名称
     */
    @TableField("coin_name")
    @ApiModelProperty(value = "币种名称", name = "coinName", required = true)
    private String coinName;
    /**
     * 提币地址名称
     */
    @ApiModelProperty(value = "提币地址名称", name = "name", required = true)
    private String name;
    /**
     * 地址
     */
    @ApiModelProperty(value = "地址", name = "addr", required = true)
    private String addr;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序", name = "sort", required = true)
    private Integer sort;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态", name = "status", required = true)
    private Integer status;
    /**
     * 更新时间
     */
    @TableField("last_update_time")
    @ApiModelProperty(value = "更新时间", name = "lastUpdateTime", required = true)
    private Date lastUpdateTime;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", name = "created", required = true)
    private Date created;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
