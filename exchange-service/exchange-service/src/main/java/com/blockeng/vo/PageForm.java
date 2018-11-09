package com.blockeng.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageForm {

    @ApiModelProperty(value = "页码", name = "current", example = "1")
    @NotEmpty(message = "页码不能为空")
    private int current;

    @ApiModelProperty(value = "分页大小", name = "size", example = "10")
    @NotEmpty(message = "分页大小不能为空")
    private int size;
}
