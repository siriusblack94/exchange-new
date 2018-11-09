package com.blockeng.admin.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 权限配置
 * </p>
 *
 * @author qiang
 * @since 2018-05-11
 */
@Data
@Accessors(chain = true)
@TableName("sys_privilege")
public class SysPrivilege extends Model<SysPrivilege> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;
    @TableField("menu_id")
    private Long menuId;
    private String name;
    private String description;
    private String url;
    private String method;
    /**
     * 创建人
     */
    @TableField("create_by")
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createBy;
    /**
     * 修改人
     */
    @TableField("modify_by")
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Long modifyBy;
    /**
     * 当前角色是否拥有该权限（1拥有，NULL 无）
     */
    @TableField(exist = false)
    private int own;
    /**
     * 创建时间
     */
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
