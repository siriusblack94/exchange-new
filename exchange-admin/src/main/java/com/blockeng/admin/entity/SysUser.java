package com.blockeng.admin.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 平台用户
 * </p>
 *
 * @author qiang
 * @since 2018-05-11
 */
@Data
@Accessors(chain = true)
@TableName("sys_user")
public class SysUser extends Model<SysUser> implements UserDetails {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;
    /**
     * 账号
     */
    @ApiModelProperty(value = "用户名", name = "username", example = "", required = true)
    private String username;
    /**
     * 密码
     */
    //@JsonIgnore
    @ApiModelProperty(value = "密码", name = "password", example = "", required = true)
    private String password;
    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名", name = "fullname", example = "", required = true)
    private String fullname;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号", name = "mobile", example = "", required = true)
    private String mobile;
    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱", name = "email", example = "", required = true)
    private String email;
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
    @ApiModelProperty(value = "创建时间", name = "created", example = "", required = false)
    private Date created;
    /**
     * 修改时间
     */
    @TableField("last_update_time")
    private Date lastUpdateTime;
    /**
     * 用户权限集合
     */
    @TableField(exist = false)
    private Collection<? extends GrantedAuthority> authorities;
    /**
     * 用户菜单集合
     */
    @TableField(exist = false)
    private List<SysMenu> menus;
    /**
     * 用户角色列表
     */
    @TableField(exist = false)
    private List<SysRole> roles;
    /**
     * 菜单树
     */
    @TableField(exist = false)
    @JsonProperty("menu_strings")
    private List<String> menuStrings;
    /**
     * 角色字符串
     */
    @TableField(exist = false)
    @JsonProperty("role_strings")
    private String roleStrings;
    /**
     * 用户授权令牌
     */
    @TableField(exist = false)
    private String token;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    /**
     * 返回分配给用户的角色列表
     *
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    /**
     * 账户是否未过期
     *
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账户是否未锁定
     *
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 密码是否未过期
     *
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 账户是否激活
     *
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
