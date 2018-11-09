package com.blockeng.extend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
 * 用户表
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Data
@Accessors(chain = true)
@TableName("user")
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    @ApiModelProperty(value = "Id", name = "id", example = "1")
    @JsonSerialize(using = ToStringSerializer.class)
    private String id;
    /**
     * 用户类型：1-普通用户；2-代理人
     */
    @ApiModelProperty(value = "用户类型：1-普通用户；2-代理人", name = "type", example = "1")
    private String type;
    /**
     * 用户名
     */
    @ApiModelProperty(name = "用户名", value = "username", example = "James")
    private String username;
    /**
     * 国际电话区号
     */
    @TableField("country_code")
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
    private String password;
    /**
     * 交易密码
     */
    @ApiModelProperty(name = "交易密码", value = "paypassword", example = "e10adc3949ba59abbe56e057f20f883e")
    private String paypassword;
    /**
     * 交易密码设置状态
     */
    @TableField("paypass_setting")
    @ApiModelProperty(value = "交易密码设置状态", name = "paypassSetting", example = "1")
    private String paypassSetting;
    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱", name = "email", example = "vip@qq.com")
    private String email;
    /**
     * 真实姓名
     */
    @TableField("real_name")
    @ApiModelProperty(value = "真实姓名", name = "realName", example = "拉里.佩奇")
    private String realName;
    /**
     * 证件类型:1，身份证；2，军官证；3，护照；4，台湾居民通行证；5，港澳居民通行证；9，其他；
     */
    @TableField("id_card_type")
    @ApiModelProperty(value = "证件类型:1，身份证；2，军官证；3，护照；4，台湾居民通行证；5，港澳居民通行证；9，其他；", name = "idCardType", example = "1")
    private String idCardType;
    /**
     * 认证状态：0-未认证；1-初级实名认证；2-高级实名认证
     */
    @TableField("auth_status")
    @ApiModelProperty(value = "认证状态：0-未认证；1-初级实名认证；2-高级实名认证", name = "authStatus", example = "2")
    private String authStatus;
    /**
     * Google令牌秘钥
     */
    @TableField("ga_secret")
    @ApiModelProperty(value = "Google令牌秘钥", name = "gaSecret", example = "121jdlASDJASOisdofas")
    @JsonIgnore
    private String gaSecret;
    /**
     * 身份证号
     */
    @TableField("id_card")
    @ApiModelProperty(value = "身份证号", name = "idCard", example = "44010091021920192019021")
    private String idCard;
    /**
     * 代理商级别
     */
    @ApiModelProperty(value = "代理商级别", name = "level", example = "1")
    private String level;
    /**
     * 认证时间
     */
    @ApiModelProperty(value = "认证时间", name = "authtime", example = "2018-05-19 23:51:09")
    private Date authtime;
    /**
     * 登录数
     */
    @ApiModelProperty(value = "登录数", name = "logins", example = "26")
    private String logins;
    /**
     * 状态：0，禁用；1，启用；
     */
    @ApiModelProperty(value = "状态：0，禁用；1，启用；", name = "status", example = "1")
    private String status;
    /**
     * 邀请码
     */
    @TableField("invite_code")
    @ApiModelProperty(value = "邀请码", name = "inviteCode", example = "6273EWE")
    private String inviteCode;
    /**
     * 邀请关系
     */
    @TableField("invite_relation")
    @ApiModelProperty(value = "邀请关系", name = "inviteRelation", example = "1113,232,31,11")
    private String inviteRelation;
    /**
     * 直接邀请人ID
     */
    @TableField("direct_inviteid")
    @ApiModelProperty(value = "直接邀请人ID", name = "directInviteid", example = "1113")
    private String directInviteid;
    /**
     * 0 否 1是  是否开启平台币抵扣手续费
     */
    @TableField("is_deductible")
    @ApiModelProperty(value = "0 否 1是  是否开启平台币抵扣手续费", name = "isDeductible", example = "1")
    private String isDeductible;
    /**
     * 审核状态,1通过,2拒绝,0,待审核
     */
    @TableField("reviews_status")
    @ApiModelProperty(value = "审核状态,1通过,2拒绝,0,待审核", name = "reviewsStatus", example = "1")
    private String reviewsStatus;
    /**
     * Google认证开启状态,0,未启用，1启用
     */
    @TableField("ga_status")
    @ApiModelProperty(value = "Google认证开启状态,0,未启用，1启用", name = "gaStatus", example = "1")
    private String gaStatus;
    /**
     * 代理商拒绝原因
     */
    @TableField("agent_note")
    @ApiModelProperty(value = "代理商拒绝原因", name = "agentNote", example = "原因未明")
    private String agentNote;
    /**
     * API的KEY
     */
    @TableField("access_key_id")
    @ApiModelProperty(value = "API的KEY", name = "accessKeyId", example = "xqweqeddththdddqdddwewew")
    private String accessKeyId;

    /**
     * 最后一次更新审核的id
     */
    @TableField("refe_auth_id")
    @ApiModelProperty(value = "最后一次审核更新", name = "refeAuthId", example = "21211221")
    private String refeAuthId;
    /**
     * API的密钥
     */
    @TableField("access_key_secret")
    @ApiModelProperty(value = "API的密钥", name = "accessKeySecret", example = "xqweqeddththdddqdddwewew")
    private String accessKeySecret;
    /**
     * 修改时间
     */
    @TableField("last_update_time")
    @ApiModelProperty(value = "认证时间", name = "authtime", example = "2018-05-19 23:51:09")
    private Date lastUpdateTime;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "认证时间", name = "authtime", example = "2018-05-19 23:51:09")
    private Date created;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
