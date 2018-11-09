package com.blockeng.admin.entity;

import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;

import java.io.Serializable;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "用户表")
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */

    @TableId(value = "id", type = IdType.ID_WORKER)
    @ApiModelProperty(value = "主键id", name = "id", required = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 用户类型：1-普通用户；2-代理人
     */
    @ApiModelProperty(value = "用户类型：1-普通用户；2-代理人", name = "type", required = true)
    private Integer type;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", name = "username", required = true)
    private String username;
    /**
     * 国际电话区号
     */
    @ApiModelProperty(value = "国际电话区号", name = "countryCode", required = true)
    @TableField("country_code")
    private String countryCode;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号", name = "mobile", required = true)

    private String mobile;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", name = "password", required = true)

    private String password;
    /**
     * 交易密码
     */
    @ApiModelProperty(value = "交易密码", name = "paypassword", required = true)

    private String paypassword;
    /**
     * 交易密码设置状态
     */
    @ApiModelProperty(value = "交易密码设置状态", name = "paypassSetting", required = true)

    @TableField("paypass_setting")
    private Integer paypassSetting;
    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱", name = "email", required = true)

    private String email;
    /**
     * 真实姓名
     */
    @ApiModelProperty(value = "真实姓名", name = "realName", required = true)

    @TableField("real_name")
    private String realName;
    /**
     * 证件类型:1，身份证；2，军官证；3，护照；4，台湾居民通行证；5，港澳居民通行证；9，其他；
     */
    @ApiModelProperty(value = " 证件类型:1，身份证；2，军官证；3，护照；4，台湾居民通行证；5，港澳居民通行证；9，其他；", name = "idCardType", required = true)
    @TableField("id_card_type")
    private Integer idCardType;
    /**
     * 认证状态：0-未认证；1-初级实名认证；2-高级实名认证
     */
    @ApiModelProperty(value = "  认证状态：0-未认证；1-初级实名认证；2-高级实名认证；", name = "authStatus", required = true)

    @TableField("auth_status")
    private Integer authStatus;
    /**
     * Google令牌秘钥
     */
    @ApiModelProperty(value = " Google令牌秘钥", name = "gaSecret", required = false)

    @TableField("ga_secret")
    private String gaSecret;
    /**
     * 身份证号
     */
    @ApiModelProperty(value = "身份证号", name = "idCard", required = true)

    @TableField("id_card")
    private String idCard;
    /**
     * 代理商级别
     */
    @ApiModelProperty(value = "代理商级别", name = "level", required = true)

    private Integer level;
    /**
     * 认证时间
     */
    @ApiModelProperty(value = "认证时间", name = "authtime", required = false)

    private Date authtime;
    /**
     * 登录数
     */
    @ApiModelProperty(value = "登录数", name = "logins", required = false)

    private Integer logins;
    /**
     * 状态：0，禁用；1，启用；
     */
    @ApiModelProperty(value = "状态：0，禁用；1，启用；", name = "status", required = true)

    private Integer status;
    /**
     * 邀请码
     */
    @ApiModelProperty(value = "邀请码；", name = "inviteCode", required = false)

    @TableField("invite_code")
    private String inviteCode;
    /**
     * 最后一次更新审核的id
     */
    @TableField("refe_auth_id")
    @ApiModelProperty(value = "最后一次审核更新", name = "refeAuthId", example = "21211221")
    private Long refeAuthId;
    /**
     * 邀请关系
     */
    @ApiModelProperty(value = "邀请关系；", name = "inviteRelation", required = false)

    @TableField("invite_relation")
    private String inviteRelation;
    /**
     * 直接邀请人ID
     */
    @ApiModelProperty(value = "直接邀请人ID；", name = "directInviteid", required = false)

    @TableField("direct_inviteid")
    private String directInviteid;
    /**
     * 0 否 1是  是否开启平台币抵扣手续费
     */
    @ApiModelProperty(value = "0 否 1是  是否开启平台币抵扣手续费", name = "isDeductible", required = false)

    @TableField("is_deductible")
    private Integer isDeductible;
    /**
     * 审核状态,1通过,2拒绝,0,待审核
     */
    @ApiModelProperty(value = "审核状态,1通过,2拒绝,0,待审核", name = "reviewsStatus", required = false)

    @TableField("reviews_status")
    private Integer reviewsStatus;
    /**
     * 代理商拒绝原因
     */
    @ApiModelProperty(value = "代理商拒绝原因", name = "agentNote", required = false)

    @TableField("agent_note")
    private String agentNote;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间", name = "lastUpdateTime", required = false)

    @TableField("last_update_time")
    private Date lastUpdateTime;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", name = "created", required = false)

    private Date created;
    /**
     * API_KEY
     */
    @TableField("access_key_id")
    private String accessKeyId;
    /**
     * API的密钥
     */
    @TableField("access_key_secret")
    private String accessKeySecret;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
