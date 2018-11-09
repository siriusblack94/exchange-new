package com.blockeng.admin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/5/28 下午6:00
 * @Modified by: Chen Long
 */
@Data
@Accessors(chain = true)
public class UpdateTradeAreaStatusDTO {

    /**
     * 交易区域ID
     */
    @NotNull(message = "交易区域ID不能为空")
    @ApiModelProperty(name = "id", value = "交易区域ID", example = "1")
    private Long id;

    /**
     * 状态
     */
    @NotNull(message = "状态不能为空")
    @ApiModelProperty(name = "status", value = "状态：1-启用；0-禁用", example = "1")
    private Integer status;
}
