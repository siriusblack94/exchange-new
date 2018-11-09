package com.blockeng.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWalletForm extends PageForm {

    @NotEmpty(message = "币种不能为空")
    @ApiModelProperty(value = "币种ID", name = "coinId", example = "1", required = true)
    private int coinId;
}
