package com.blockeng.web.vo;

import com.blockeng.framework.enums.UserType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author qiang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "登录表单内容", description = "登录表单内容")
public class UserLoginForm implements java.io.Serializable {

    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名", name = "username", example = "qiang")
    private String username;

    /**
     * 国际电话区号
     */
    @NotEmpty(message = "国际电话区号不能为空")
    @ApiModelProperty(value = "国际电话区号", name = "countryCode", example = "+86")
    private String countryCode;

    /**
     * 手机号
     */
    /*
    @NotEmpty(message="手机号码不能为空")
    @Length(min = 11, max = 11,message="手机号码格式不正确")
    @ApiModelProperty(value="手机号",name="mobile",example="13312979999")
    private String mobile;*/

    /**
     * 邮箱
     */
    /*
    @ApiModelProperty(value="邮箱",name="email",example="qiang.ins@gmail.com")
    private String email;*/

    /**
     * 密码
     */
    @NotEmpty(message = "密码不能为空")
    @Length(min = 6, message = "密码长度不能小于6位")
    @ApiModelProperty(value = "密码", name = "password", example = "e10adc3949ba59abbe56e057f20f883e")
    private String password;

    /**
     * 动态口令
     */
    private Integer ga_code;

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

    /**
     * 用户类型
     */
    @NotNull(message = "用户类型不能为空")
    @ApiModelProperty(value = "用户类型", name = "type", example = "1")
    private Integer type = UserType.CUSTOMER.getCode();

   /* @NotNull(message = "手机验证码不能为空")
    @ApiModelProperty(value="验证码",name="validateCode",example = "1234")
    private String validateCode;*/
}