package com.blockeng.admin.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户权限配置
 * </p>
 *
 * @author qiang
 * @since 2018-05-26
 */
@Data
@Accessors(chain = true)
@TableName("sys_user_role_privilege")
public class SysUserRolePrivilege extends Model<SysUserRolePrivilege> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 角色Id
     */
    @TableField("role_id")
    private Long roleId;
    /**
     * 用户Id
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 权限Id
     */
    @TableField("privilege_id")
    private Long privilegeId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
