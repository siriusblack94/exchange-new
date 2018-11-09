package com.blockeng.web.vo;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetPasswordForm {

    /**
     * 国际电话区号
     */

    private String countryCode;
    /***
     * 新密码
     */
    @JSONField(serialize = false)
    @NotEmpty(message = "新密码不能为空")
    @Length(min = 6, message = "旧密码长度不能小于6位")
    private String password;


    @JSONField(serialize = false)
    private String mobile;

    private String email;

    /**
     * 短信验证码
     */
    @NotEmpty(message = "短信验证码不能为空")
    private String validateCode;

}
