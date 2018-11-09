package com.blockeng.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CashStatusForm extends PageForm {

    @ApiModelProperty(value = "状态", name = "status", example = "1")
    private Integer status;
}
