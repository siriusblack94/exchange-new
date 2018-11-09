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
public class ChangeEmailForm implements java.io.Serializable {


    /***
     * 旧的邮箱验证码
     */
    @NotEmpty(message = "修改短信")
    private String oldValidateCode;

    /***
     * 新的邮箱验
     */
    @NotEmpty(message = "新的邮箱")
    private String newEmail;

    /**
     * 新的邮箱验验证码
     */
    @NotEmpty(message = "邮箱验证码不能为空")
    private String validateCode;
}