package com.blockeng.admin.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.google.common.base.Strings;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 系统菜单
 * </p>
 *
 * @author qiang
 * @since 2018-05-11
 */
@Data
@Accessors(chain = true)
@TableName("sys_menu")
public class SysMenu extends Model<SysMenu> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;
    /**
     * 上级菜单ID
     */
    @TableField("parent_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;
    /**
     * 上级菜单唯一KEY值
     */
    @TableField("parent_key")
    private String parentKey;
    /**
     * 类型 1-分类 2-节点
     */
    private Integer type;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称", name = "name", example = "", required = true)
    private String name;
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述", name = "desc", example = "", required = true)
    private String desc;
    /**
     * 目标地址
     */
    @TableField("target_url")
    @ApiModelProperty(value = "目标地址", name = "targetUrl", example = "", required = true)
    private String targetUrl;
    /**
     * 排序索引
     */
    @ApiModelProperty(value = "排序", name = "sort", example = "", required = true)
    private Integer sort;
    /**
     * 状态 0-无效； 1-有效；
     */
    @ApiModelProperty(value = "状态", name = "status", example = "0 禁用 1 启用", required = true)
    private Integer status;
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
     * 创建时间
     */
    private Date created;
    /**
     * 修改时间
     */
    @TableField("last_update_time")
    private Date lastUpdateTime;
    /**
     * 子菜单
     */
    @TableField(exist = false)
    private List<SysMenu> childs;
    /**
     * 菜单下的功能点
     */
    @TableField(exist = false)
    private List<SysPrivilege> privileges;

    public String getMenuKey() {
        if (!Strings.isNullOrEmpty(parentKey)) {
            return parentKey + "." + id;
        } else {
            return id.toString();
        }
    }

    /**
     * 菜单的唯一Key值
     */
    @TableField(exist = false)
    private String menuKey;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
