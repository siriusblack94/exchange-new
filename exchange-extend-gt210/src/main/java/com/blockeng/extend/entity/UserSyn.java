package com.blockeng.extend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class UserSyn {
    /**
     * 用户id
     */
    @TableId(value = "id")
    @JsonSerialize(using = ToStringSerializer.class)
    private String id;

    /**
     * 邀请人[上级]user_id
     */
    @TableId(value = "parent_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private String parentId;
    /**
     * 账号
     */
    @TableId(value = "account")
    private String account;
    /**
     * 密码
     */
    @TableField("password")
    private String password;
    /**
     * 手机
     */
    @TableField("mobile")
    private String mobile;
    /**
     * 邮箱
     */
    @TableField("mail")
    private String mail;
    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;
    /**
     * 真实用户名
     */
    @TableField("real_name")
    private String realName;
    /**
     * 注册时间
     */
    @TableField("ctime")
    private String ctime;
    /**
     * 修改时间
     */
    @TableField("utime")
    private String utime;

    /**
     * 同步状态:0失败，1成功
     */
    @TableField("status")
    private String status;
    /**
     * 外部系统返回token
     */
    @TableField("token")
    private String token;

    @TableField("message")
    private String message;


    public UserSyn getSynUser(User user){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            this.setId(user.getId())
                .setParentId(user.getDirectInviteid())
                .setAccount(StringUtils.isNotEmpty(user.getMobile())?user.getMobile():user.getEmail())
                .setPassword(user.getPassword())
                .setMail(StringUtils.isNotEmpty(user.getEmail())?user.getEmail():StringUtils.EMPTY)
                .setMobile(StringUtils.isNotEmpty(user.getMobile())?user.getMobile():StringUtils.EMPTY)
                .setUserName(StringUtils.isNotEmpty(user.getUsername())?user.getUsername():StringUtils.EMPTY)
                .setRealName(user.getRealName())
                .setUtime(user.getLastUpdateTime()!=null?formatter.format(user.getLastUpdateTime()):null)
                .setCtime(user.getCreated()!=null?formatter.format(user.getCreated()):null);
            return this;
    }
}