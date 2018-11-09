package com.blockeng.web.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author qiang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoogleAuthenticatorVerifyForm implements java.io.Serializable {

    /**
     * 密钥
     */
    @NotEmpty(message = "密钥不能为空")
    private String secret;

    /**
     * 验证码
     */
    @NotNull
    private Integer code;
}