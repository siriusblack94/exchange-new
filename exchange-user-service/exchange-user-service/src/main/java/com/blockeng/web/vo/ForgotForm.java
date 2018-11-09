package com.blockeng.web.vo;

import com.alibaba.fastjson.annotation.JSONField;
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
public class ForgotForm implements java.io.Serializable {

    /**
     * 国际电话区号
     */
    @NotEmpty(message = "国际电话区号不能为空")
    private String countryCode;
    /**
     * 手机号
     */
    @NotEmpty(message = "手机号码不能为空")
    @Length(min = 11, max = 11, message = "手机号码格式不正确")
    private String mobile;

    /***
     * 密码
     */
    @JSONField(serialize = false)
    @NotEmpty(message = "密码不能为空")
    @Length(min = 6, message = "密码长度不能小于6位")
    private String password;
    /**
     * 短信验证码
     */
    @NotEmpty(message = "短信验证码不能为空")
    private String validateCode;

    /**
     * 验证码
     */
    private String code;
}