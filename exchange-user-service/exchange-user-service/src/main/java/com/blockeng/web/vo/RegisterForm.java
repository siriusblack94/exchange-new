package com.blockeng.web.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * @author qiang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterForm implements java.io.Serializable {

    /**
     * 用户名
     */
    private String userName;
    /**
     * 国际电话区号
     */
    @NotEmpty(message = "国际电话区号不能为空")
    private String countryCode;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 密码
     */
    @NotEmpty(message = "密码不能为空")
    @Length(min = 6, message = "密码长度不能小于6位")
    private String password;
    /**
     * 短信验证码
     */
    @NotEmpty(message = "短信验证码不能为空")
    private String validateCode;
    /**
     * 邀请码
     */
    private String invitionCode;

    /**
     * 极验验证二次验证表单数据 chllenge
     */
    @NotEmpty(message = "极验验证二次验证表单数据 chllenge")
    private String geetest_challenge;
    /**
     * 极验验证二次验证表单数据 validate
     */
    @NotEmpty(message = "极验验证二次验证表单数据 validate")
    private String geetest_validate;
    /**
     * 极验验证二次验证表单数据 seccode
     */
    @NotEmpty(message = "极验验证二次验证表单数据 seccode")
    private String geetest_seccode;


    private String platid;
}