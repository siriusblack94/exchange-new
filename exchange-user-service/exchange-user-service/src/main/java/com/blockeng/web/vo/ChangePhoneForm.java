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
public class ChangePhoneForm implements java.io.Serializable {


    /***
     * 旧手机号码验证码
     */
    @NotEmpty(message = "修改短信")
    private String oldValidateCode;
    /***
     * 新手机号码
     */
    @NotEmpty(message = "新手机号")
    @Length(min = 11, max = 11, message = "手机号码格式不正确")
    private String newMobilePhone;

    @NotEmpty(message = "区号不能为空")
    private String countryCode;

    /**
     * 短信验证码
     */
    @NotEmpty(message = "短信验证码不能为空")
    private String validateCode;
}