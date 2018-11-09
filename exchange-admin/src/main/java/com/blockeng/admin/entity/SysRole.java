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
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色
 * </p>
 *
 * @author qiang
 * @since 2018-05-11
 */
@Data
@Accessors(chain = true)
@TableName("sys_role")
public class SysRole extends Model<SysRole> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;
    /**
     * 名称
     */
    @ApiModelProperty(value = "角色名称", name = "name", example = "", required = true)
    private String name;
    /**
     * 代码
     */
    @ApiModelProperty(value = "角色代码", name = "code", example = "", required = true)
    private String code;
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述", name = "description", example = "", required = true)
    private String description;
    /**
     * 创建人
     */
    @TableField("create_by")
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Long createBy;
    /**
     * 修改人
     */
    @TableField("modify_by")
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Long modifyBy;
    /**
     * 状态0:禁用 1:启用
     */
    @ApiModelProperty(value = "状态", name = "status", example = "0 禁用 1 启用", required = true)
    private Integer status;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", name = "created", example = "", required = false)
    private Date created;
    /**
     * 修改时间
     */
    @TableField("last_update_time")
    private Date lastUpdateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
