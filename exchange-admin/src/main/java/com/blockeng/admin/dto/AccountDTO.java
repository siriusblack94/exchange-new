package com.blockeng.admin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Create Time: 2018年06月05日 21:02
 * 虚拟充币
 *
 * @author lxl
 * @Dec
 **/
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class AccountDTO {

    /**
     * 用户ID
     */
    @NotNull(message = "用户id不能为空")
    @ApiModelProperty(name = "userId", value = "用户id", example = "123")
    private Long userId;

    /**
     * 币种ID
     */
    @NotNull(message = "币种id不能为空")
    @ApiModelProperty(name = "coinId", value = "币种id")
    private Long coinId;

    /**
     * 充值数额
     */
    @NotNull(message = "充值数量不能为空")
    @ApiModelProperty(name = "amount", value = "充值数额", example = "100")
    private BigDecimal amount;

    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;
}
