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
public class CashRechargeForm {

    @NotNull(message = "数量不能为空")
    @ApiModelProperty(value = "充值数量", name = "num", example = "1", required = true)
    private BigDecimal num;

    @NotNull(message = "总金额不能为空")
    @ApiModelProperty(value = "充值金额", name = "mun", example = "1", required = true)
    private BigDecimal mum;

    @NotNull(message = "币种不能为空")
    @ApiModelProperty(value = "币种ID", name = "coinId", example = "1", required = true)
    private Long coinId;
}
