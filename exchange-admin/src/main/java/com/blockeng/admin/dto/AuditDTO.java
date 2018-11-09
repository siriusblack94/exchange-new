package com.blockeng.admin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

/**
 * @Description: 审核请求参数
 * @Author: Chen Long
 * @Date: Created in 2018/5/26 下午9:06
 * @Modified by: Chen Long
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class AuditDTO {

    /**
     * 订单号
     */
    @NotEmpty(message = "订单号不能为空")
    @ApiModelProperty(name = "id", value = "订单号", example = "123")
    private Long id;

    /**
     * 审核状态
     */
    @NotEmpty(message = "审核状态不能为空")
    @ApiModelProperty(name = "status", value = "审核状态：1-通过；2-拒绝", example = "1")
    private Integer status;

    /**
     * 审核备注
     */
    @ApiModelProperty(name = "remark", value = "审核备注", example = "资金账户异常")
    private String remark;
}
