package com.blockeng.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteWalletForm {


    @NotEmpty(message = "钱包地址ID为空")
    @ApiModelProperty(value = "钱包地址ID", name = "addressId", example = "1", required = true)
    private long addressId;

    @NotEmpty(message = "支付密码不能为空")
    @ApiModelProperty(value = "支付密码", name = "payPassword", example = "1", required = true)
    private String payPassword;
}
