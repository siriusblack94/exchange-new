package com.blockeng.extend.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * @Auther: sirius
 * @Date: 2018/11/6 16:00
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetryForm {
    /**
     * id
     */
    private String id;
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

}
