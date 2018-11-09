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
public class ChangePayPasswordForm {

    /***
     * 旧密码
     */
    @JSONField(serialize = false)
    @NotEmpty(message = "旧密码不能为空")
    @Length(min = 6, message = "旧密码长度不能小于6位")
    private String payPassword;

    /**
     * 短信验证码
     */
    @NotEmpty(message = "短信验证码不能为空")
    private String validateCode;


    private String email;

}
