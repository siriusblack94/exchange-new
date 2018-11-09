package com.blockeng.web.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * @author qiang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApikeyGetForm {

    /**
     * 短信验证码
     */
    @NotEmpty(message = "短信验证码不能为空")
    private String validateCode;
}
