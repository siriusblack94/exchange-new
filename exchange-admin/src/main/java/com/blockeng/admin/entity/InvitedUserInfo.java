package com.blockeng.admin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
@ApiModel(description = "被邀请用户")
public class InvitedUserInfo {


    /**
     * id
     */
    @ApiModelProperty(value = "id", name = "id", required = false)
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
    private String countryCode;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号", name = "mobile", required = true)
    private String mobile;
    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱", name = "email", required = true)
    private String email;
    /**
     * 真实姓名
     */
    @ApiModelProperty(value = "真实姓名", name = "realName", required = true)
    private String realName;
    /**
     * 证件类型:1，身份证；2，军官证；3，护照；4，台湾居民通行证；5，港澳居民通行证；9，其他；
     */
    @ApiModelProperty(value = " 证件类型:1，身份证；2，军官证；3，护照；4，台湾居民通行证；5，港澳居民通行证；9，其他；", name = "idCardType", required = true)
    private Integer idCardType;
    /**
     * 认证状态：0-未认证；1-初级实名认证；2-高级实名认证
     */
    @ApiModelProperty(value = "  认证状态：0-未认证；1-初级实名认证；2-高级实名认证；", name = "authStatus", required = true)
    private Integer authStatus;
    /**
     * Google令牌秘钥
     */
    @ApiModelProperty(value = " Google令牌秘钥", name = "gaSecret", required = false)
    private String gaSecret;
    /**
     * 身份证号
     */
    @ApiModelProperty(value = "身份证号", name = "idCard", required = true)
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
    private String inviteCode;
    /**
     * 最后一次更新审核的id
     */
    @ApiModelProperty(value = "最后一次审核更新", name = "refeAuthId", example = "21211221")
    private Long refeAuthId;
    /**
     * 邀请关系
     */
    @ApiModelProperty(value = "邀请关系；", name = "inviteRelation", required = false)
    private String inviteRelation;
    /**
     * 直接邀请人ID
     */
    @ApiModelProperty(value = "直接邀请人ID；", name = "directInviteid", required = false)
    private String directInviteid;
    /**
     * 0 否 1是  是否开启平台币抵扣手续费
     */
    @ApiModelProperty(value = "0 否 1是  是否开启平台币抵扣手续费", name = "isDeductible", required = false)
    private Integer isDeductible;
    /**
     * 审核状态,1通过,2拒绝,0,待审核
     */
    @ApiModelProperty(value = "审核状态,1通过,2拒绝,0,待审核", name = "reviewsStatus", required = false)
    private Integer reviewsStatus;
    /**
     * 代理商拒绝原因
     */
    @ApiModelProperty(value = "代理商拒绝原因", name = "agentNote", required = false)
    private String agentNote;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间", name = "lastUpdateTime", required = false)
    private Date lastUpdateTime;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", name = "created", required = false)
    private Date created;
    /**
     * API_KEY
     */
    private String accessKeyId;
    /**
     * API的密钥
     */
    private String accessKeySecret;

    /**
     * 持有的平台币数量
     */
    private BigDecimal amount;

}
