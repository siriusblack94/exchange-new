package com.blockeng.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author qiang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RandCode implements Serializable {

    /**
     * 国际电话区号
     */
    @NotEmpty(message = "国际电话区号不能为空")
    private String countryCode;
    /**
     * 模板Id
     */
    private String templateCode;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
}