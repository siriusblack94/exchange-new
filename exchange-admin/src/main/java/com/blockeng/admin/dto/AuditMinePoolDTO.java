package com.blockeng.admin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @Description: 矿池审核请求参数
 * @Author: Chen Long
 * @Date: Created in 2018/6/29 下午5:20
 * @Modified by: Chen Long
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class AuditMinePoolDTO {

    /**
     * ID
     */
    @NotNull(message = "ID不能为空")
    @ApiModelProperty(name = "id", value = "id", example = "123")
    private Long id;

    /**
     * 状态
     */
    @NotNull(message = "审核状态不能为空")
    @ApiModelProperty(name = "status", value = "状态", example = "状态：1-审核通过；2-审核拒绝")
    private int status;

    /**
     * 备注
     */
    @NotNull(message = "审核备注不能为空")
    @ApiModelProperty(name = "remark", value = "备注", example = "审核备注")
    private String remark;
}
