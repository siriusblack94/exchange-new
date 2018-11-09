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
public class GoogleAuthenticatorCancelForm implements java.io.Serializable {

    /**
     * 验证码
     */
    @NotNull
    private Integer code;
}