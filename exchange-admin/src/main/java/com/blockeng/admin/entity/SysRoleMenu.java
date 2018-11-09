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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色菜单
 * </p>
 *
 * @author qiang
 * @since 2018-05-11
 */
@Data
@Accessors(chain = true)
@TableName("sys_role_menu")
public class SysRoleMenu extends Model<SysRoleMenu> {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;
    @TableField("role_id")
    private Long roleId;
    @TableField("menu_id")
    private Long menuId;
    /**
     * 创建人
     */
    @TableField("create_by")
    private Long createBy;
    /**
     * 修改人
     */
    @TableField("modify_by")
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


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
