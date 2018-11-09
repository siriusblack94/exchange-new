package com.blockeng.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

/**
 * @author qiang
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class RandCodeVerifyDTO {

    /**
     * 国际电话区号
     */
    @NotEmpty(message = "国际电话区号不能为空")
    private String countryCode;

    @NotEmpty(message = "templateCode不能为空")
    private String templateCode;

    @NotEmpty(message = "手机号码不能为空")
    private String phone;

    private String email;

    private String code;
}
