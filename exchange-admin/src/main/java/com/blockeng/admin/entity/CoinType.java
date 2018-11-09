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
 * 币种类型
 * </p>
 *
 * @author Haliyo
 * @since 2018-05-22
 */
@Data
@Accessors(chain = true)
@TableName("coin_type")
public class CoinType extends Model<CoinType> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 代码
     */
    @ApiModelProperty(value = "code", name = "code", example = "", required = false)
    private String code;
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述", name = "description", example = "", required = false)
    private String description;
    /**
     * 状态：0-无效；1-有效；
     */
    @ApiModelProperty(value = "状态", name = "status", example = "0 无效 1 有效", required = false)
    private Integer status;
    /**
     * 创建时间
     */
    private Date created;
    /**
     * 更新时间
     */
    @TableField("last_update_time")
    private Date lastUpdateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
