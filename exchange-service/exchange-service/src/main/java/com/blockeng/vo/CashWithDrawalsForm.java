package com.blockeng.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CashWithDrawalsForm {

    @NotNull(message = "币种不能为空")
    @ApiModelProperty(value = "币种ID不能为空", name = "coinId", example = "1", required = true)
    private Long coinId;

    @NotNull(message = "提现数量不能为空")
    @ApiModelProperty(value = "提现数量", name = "num", example = "1", required = true)
    private BigDecimal num;

    @NotNull(message = "提现总额不能为空")
    @ApiModelProperty(value = "提现金额", name = "mum", example = "1", required = true)
    private BigDecimal mum;

    @NotEmpty(message = "验证码不能为空")
    @ApiModelProperty(value = "手机验证码", name = "validateCode", example = "1111", required = true)
    private String validateCode;

    @NotEmpty(message = "支付密码不能为空")
    @ApiModelProperty(value = "支付密码", name = "payPassword", example = "e10adc3949ba59abbe56e057f20f883e", required = true)
    private String payPassword;


}
