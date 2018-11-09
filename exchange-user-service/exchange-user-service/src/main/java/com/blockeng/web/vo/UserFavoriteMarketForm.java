package com.blockeng.web.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用户收藏交易市场", description = "用户收藏交易市场")
public class UserFavoriteMarketForm {


    @NotEmpty(message = "收藏交易市场不能为空")
    @ApiModelProperty(value = "交易市场symbol", name = "symbol", example = "BTCUSDT")
    private String symbol;

    @NotEmpty(message = "交易市场类型不能为空")
    @ApiModelProperty(value = "交易市场类型 1数字货币 2创新交易", name = "type", example = "1")
    private long type;
}
