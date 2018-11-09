package com.blockeng.framework.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Date;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "用户", description = "用户")
public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @ApiModelProperty(value = "Id", name = "id", example = "1")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 用户类型：1-普通用户；2-代理人
     */
    @ApiModelProperty(value = "用户类型：1-普通用户；2-代理人", name = "type", example = "1")
    private Integer type;
    /**
     * 用户名
     */
    @ApiModelProperty(name = "用户名", value = "username", example = "James")
    private String username;
    /**
     * 国际电话区号
     */
    @ApiModelProperty(name = "国际电话区号", value = "countryCode", example = "+86")
    private String countryCode;
    /**
     * 手机号
     */
    @ApiModelProperty(name = "mobile", value = "手机号", example = "1333122111")
    private String mobile;
    /**
     * 密码
     */
    @ApiModelProperty(name = "password", value = "密码", example = "e10adc3949ba59abbe56e057f20f883e")
    @JsonIgnore
    private String password;
    /**
     * 交易密码
     */
    @ApiModelProperty(name = "交易密码", value = "paypassword", example = "e10adc3949ba59abbe56e057f20f883e")
    @JsonIgnore
    private String paypassword;
    /**
     * 交易密码设置状态
     */
    @ApiModelProperty(value = "交易密码设置状态", name = "paypassSetting", example = "1")
    private Integer paypassSetting;
    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱", name = "email", example = "vip@qq.com")
    private String email;
    /**
     * 真实姓名
     */
    @ApiModelProperty(value = "真实姓名", name = "realName", example = "拉里.佩奇")
    private String realName;
    /**
     * 证件类型:1，身份证；2，军官证；3，护照；4，台湾居民通行证；5，港澳居民通行证；9，其他；
     */
    @ApiModelProperty(value = "证件类型:1，身份证；2，军官证；3，护照；4，台湾居民通行证；5，港澳居民通行证；9，其他；", name = "idCardType", example = "1")
    private Integer idCardType;
    /**
     * 认证状态：0-未认证；1-初级实名认证；2-高级实名认证
     */
    @ApiModelProperty(value = "认证状态：0-未认证；1-初级实名认证；2-高级实名认证", name = "authStatus", example = "2")
    private Integer authStatus;
    /**
     * Google认证开启状态,0,未启用，1启用
     */
    @ApiModelProperty(value = "Google认证开启状态,0,未启用，1启用", name = "gaStatus", example = "1")
    private Integer gaStatus;
    /**
     * Google令牌秘钥
     */
    @ApiModelProperty(value = "Google令牌秘钥", name = "gaSecret", example = "121jdlASDJASOisdofas")
    @JsonIgnore
    private String gaSecret;
    /**
     * 身份证号
     */
    @ApiModelProperty(value = "身份证号", name = "idCard", example = "44010091021920192019021")
    private String idCard;
    /**
     * 代理商级别
     */
    @ApiModelProperty(value = "代理商级别", name = "level", example = "1")
    private Integer level;
    /**
     * 认证时间
     */
    @ApiModelProperty(value = "认证时间", name = "authtime", example = "2018-05-19 23:51:09")
    private Date authtime;
    /**
     * 登录数
     */
    @ApiModelProperty(value = "登录数", name = "logins", example = "26")
    private Integer logins;
    /**
     * 状态：0，禁用；1，启用；
     */
    @ApiModelProperty(value = "状态：0，禁用；1，启用；", name = "status", example = "1")
    private Integer status;
    /**
     * 邀请码
     */
    @ApiModelProperty(value = "邀请码", name = "inviteCode", example = "6273EWE")
    private String inviteCode;
    /**
     * 邀请关系
     */
    @ApiModelProperty(value = "邀请关系", name = "inviteRelation", example = "1113,232,31,11")
    private String inviteRelation;
    /**
     * 直接邀请人ID
     */
    @ApiModelProperty(value = "直接邀请人ID", name = "directInviteid", example = "1113")
    private String directInviteid;
    /**
     * 0 否 1是  是否开启平台币抵扣手续费
     */
    @ApiModelProperty(value = "0 否 1是  是否开启平台币抵扣手续费", name = "isDeductible", example = "1")
    private Integer isDeductible;
    /**
     * 审核状态,1通过,2拒绝,0,待审核
     */
    @ApiModelProperty(value = "审核状态,1通过,2拒绝,0,待审核", name = "reviewsStatus", example = "1")
    private Integer reviewsStatus;


    @ApiModelProperty(value = "高级认证审核状态 0待审核,1通过,2拒绝,3未上传")
    private Integer seniorAuthStatus;

    @ApiModelProperty(value = "高级认证审核原因")
    private String seniorAuthDesc;

    /**
     * 代理商拒绝原因
     */
    @ApiModelProperty(value = "代理商拒绝原因", name = "agentNote", example = "原因未明")
    private String agentNote;
    /**
     * API的KEY
     */
    @ApiModelProperty(value = "API的密钥", name = "accessKeySecret", example = "xqweqeddt")
    private String accessKeyId;
    /**
     * API的密钥
     */
    @ApiModelProperty(value = "API的密钥", name = "accessKeySecret", example = "xqweqeddththdddqdddwewew")
    @JsonIgnore
    private String accessKeySecret;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "认证时间", name = "authtime", example = "2018-05-19 23:51:09")
    private Date lastUpdateTime;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "认证时间", name = "authtime", example = "2018-05-19 23:51:09")
    private Date created;
    /**
     * 用户权限集合
     */
    private Collection<? extends GrantedAuthority> authorities;
    /**
     * 用户授权令牌
     */
    @JsonProperty("access_token")
    private String accessToken;
    /**
     * 授权颁发的刷新令牌
     */
    @JsonProperty("refresh_token")
    private String refreshToken;
    /**
     * 失效时间
     */
    private Long expire;

    /**
     * 动态token
     * */
    private String dynamicToken;

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