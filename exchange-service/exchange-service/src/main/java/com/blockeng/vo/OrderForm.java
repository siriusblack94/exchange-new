package com.blockeng.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderForm extends PageForm {

    @NotNull(message = "市场symbol")
    @ApiModelProperty(value = "市场symbol", name = "symbol", example = "1", required = true)
    private String symbol;

    @ApiModelProperty(value = "type", name = "type", example = "1")
    private int type;
}
