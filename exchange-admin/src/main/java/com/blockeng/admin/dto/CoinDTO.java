package com.blockeng.admin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Create Time: 2018年06月06日 17:53
 * 新建币种返回数据给前端
 *
 * @author lxl
 * @Dec
 **/
@Data
@Accessors(chain = true)
public class CoinDTO {

    @ApiModelProperty(value = "币种id", name = "coinId", required = false)
    private Long coinId;
    @ApiModelProperty(value = "状态值：是否可用0不可用,1可用", name = "status", required = false)
    private Integer status;//`和币种的状态一致
}
