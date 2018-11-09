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
public class VerifyOldEmailForm implements java.io.Serializable {

    /***
     * 旧的邮箱验证码
     */
    @NotEmpty(message = "旧的邮箱验证码")
    private String oldValidateCode;

}