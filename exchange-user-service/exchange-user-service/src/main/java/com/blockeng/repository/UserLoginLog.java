package com.blockeng.repository;

import com.blockeng.entity.IpInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户登录日志
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Data
@Accessors(chain = true)
@Document(collection = "user_login_log")
public class UserLoginLog implements Serializable {

    /**
     * 主键
     */
    @Id
    private String id;
    /**
     * 用户名
     */
    @ApiModelProperty(name = "用户名", value = "username", example = "James")
    private String username;
    /**
     * 国际电话区号
     */
    @Field("country_code")
    @ApiModelProperty(name = "国际电话区号", value = "countryCode", example = "+86")
    private String countryCode;
    /**
     * 手机号
     */
    @ApiModelProperty(name = "mobile", value = "手机号")
    private String mobile;
    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱", name = "email")
    private String email;
    /**
     * 用户ID
     */
    @Field("user_id")
    private Long userId;
    /**
     * 客户端类型
     * 1-PC
     * 2-IOS
     * 3-Android
     */
    @Field("user_agent")
    private String userAgent;
    /**
     * 登录IP
     */
    @Field("login_ip")
    private String loginIp;
    /**
     * 登录地址
     */
    @Field("login_address")
    private IpInfo loginAddress;
    /**
     * 登录时间
     */
    @Field("login_time")
    private Date loginTime;

    @Field("login_timestamp")
    private Long loginTimestamp;
}
