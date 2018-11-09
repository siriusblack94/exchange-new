package com.blockeng.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/5/22 下午3:31
 * @Modified by: Chen Long
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class WithdrawAddressDTO {

    /**
     * 币种ID
     */
    @NotEmpty(message = "币种不能为空")
    @ApiModelProperty(name = "coinId", value = "币种ID", example = "11111")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long coinId;

    /**
     * 提币地址名称
     */
    @NotEmpty(message = "钱包名称不能为空")
    @ApiModelProperty(name = "name", value = "提币地址名称", example = "比特币冷钱包")
    private String name;

    /**
     * 地址
     */
    @NotEmpty(message = "钱包地址不能为空")
    @ApiModelProperty(name = "address", value = "钱包地址", example = "1CK6KHY6MHgYvmRQ4PAafKYDrg1ejbH1cE")
    private String address;

    /**
     * 资金交易密码
     */
    @NotEmpty(message = "资金交易密码不能为空")
    @ApiModelProperty(name = "paymentPassword", value = "支付密码", example = "******")
    private String payPassword;
}
