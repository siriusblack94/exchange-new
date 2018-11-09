package com.blockeng.user.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author qiang
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用户DTO", description = "用户DTO")
public class UserDTO {

    /**
     * 自增id
     */
    private Long id;
    /**
     * 用户类型：1-普通用户；2-代理人
     */
    private Integer type;
    /**
     * 用户名
     */
    private String username;
    /**
     * 国际电话区号
     */
    private String countryCode;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 密码
     */
    private String password;
    /**
     * 交易密码
     */
    private String paypassword;
    /**
     * 交易密码设置状态
     */
    private Integer paypassSetting;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 证件类型:1，身份证；2，军官证；3，护照；4，台湾居民通行证；5，港澳居民通行证；9，其他；
     */
    private Integer idCardType;
    /**
     * 认证状态：0-未认证；1-初级实名认证；2-高级实名认证
     */
    private Integer authStatus;
    /**
     * Google令牌秘钥
     */
    private String gaSecret;
    /**
     * 身份证号
     */
    private String idCard;
    /**
     * 代理商级别
     */
    private Integer level;
    /**
     * 认证时间
     */
    private Date authtime;
    /**
     * 登录数
     */
    private Integer logins;
    /**
     * 状态：0，禁用；1，启用；
     */
    private Integer status;
    /**
     * 邀请码
     */
    private String inviteCode;
    /**
     * 邀请关系
     */
    private String inviteRelation;
    /**
     * 直接邀请人ID
     */
    private String directInviteid;
    /**
     * 0 否 1是  是否开启平台币抵扣手续费
     */
    private Integer isDeductible;
    /**
     * 审核状态,1通过,2拒绝,0,待审核
     */
    private Integer reviewsStatus;
    /**
     * 代理商拒绝原因
     */
    private String agentNote;
    /**
     * API的密钥
     */
    private String accessKeySecret;
    /**
     * 修改时间
     */
    private Date lastUpdateTime;
    /**
     * 创建时间
     */
    private Date created;
}
