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
 * 用户以太系列钱包地址信息
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Data
@Accessors(chain = true)
@TableName("user_address")
public class UserAddress extends Model<UserAddress> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "id", name = "id", example = "", required = false)
    private Long id;
    /**
     * 用户ID
     */
    @TableField("user_id")
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "用户id", name = "userId", example = "", required = false)

    private Long userId;
    /**
     * 币种ID
     */
    @TableField("coin_id")
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "币种id", name = "coinId", example = "", required = false)

    private Long coinId;
    @TableField(value = "coinName", exist = false)
    @ApiModelProperty(value = "币种名称", name = "coinName", example = "", required = false)
    private String coinName;
    /**
     * 地址
     */
    @ApiModelProperty(value = "地址", name = "address", example = "", required = false)

    private String address;
    /**
     * keystore
     */
    @ApiModelProperty(value = "keystore", name = "keystore", example = "", required = false)

    private String keystore;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", name = "pwd", example = "", required = false)

    private String pwd;
    /**
     * 更新时间
     */
    @TableField("last_update_time")
    private Date lastUpdateTime;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", name = "created", example = "", required = false)

    private Date created;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
