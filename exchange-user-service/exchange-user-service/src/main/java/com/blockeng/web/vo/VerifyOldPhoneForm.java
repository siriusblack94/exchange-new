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
public class VerifyOldPhoneForm implements java.io.Serializable {

    /***
     * 旧手机号码
     */
    @NotEmpty(message = "修改短信")
    @NotEmpty(message = "旧手机号短信")
    private String oldValidateCode;

}