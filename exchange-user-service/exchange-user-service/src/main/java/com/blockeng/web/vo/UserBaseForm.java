package com.blockeng.web.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用户基本信息", description = "用户基本信息")
public class UserBaseForm {


    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名", name = "username", example = "qiang")
    private String username;

    @ApiModelProperty(value = "邮箱", name = "email", example = "qiang.ins@gmail.com")
    private String email;


    @NotEmpty(message = "资金密码不能为空")
    @ApiModelProperty(value = "资金密码", name = "payPassword", example = "qiang")
    private String payPassword;


}
