package com.blockeng.framework.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Description: 提币申请
 * @Author: Chen Long
 * @Date: Created in 2018/5/22 下午5:55
 * @Modified by: Chen Long
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ApplyWithdrawDTO {

    /**
     * 短信验证码
     */
    @NotEmpty(message = "短信验证码不能为空")
    @ApiModelProperty(name = "verifyCode", value = "短信验证码", example = "123456")
    private String verifyCode;

    /**
     * 币种ID
     */
    @NotNull(message = "币种ID不能为空")
    @ApiModelProperty(name = "coinId", value = "币种ID", example = "1")
    private Long coinId;

    /**
     * 钱包地址ID
     */
    @ApiModelProperty(name = "addressId", value = "钱包地址ID", example = "123")
    private Long addressId;

    /**
     * 钱包地址ID
     */
    @NotEmpty(message = "钱包地址不能为空")
    @ApiModelProperty(name = "address", value = "钱包地址", example = "13hQVEstgo4iPQZv9C7VELnLWF7UWtF4Q3")
    private String address;

    /**
     * 资金交易密码
     */
    @NotEmpty(message = "资金交易密码不能为空")
    @ApiModelProperty(name = "payPassword", value = "资金交易密码", example = "******")
    private String payPassword;

    /**
     * 提现金额
     */
    @NotNull(message = "提现金额不能为空")
    @ApiModelProperty(name = "amount", value = "提现金额", example = "100")
    private BigDecimal amount;
}
