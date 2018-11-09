package com.blockeng.admin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 交易统计
 * Create Time: 2018年05月31日 18:22
 *
 * @author lxl
 * @Dec
 **/
@Data
@Accessors(chain = true)
public class TurnoverOrderCountDTO {
    @ApiModelProperty(value = "交易币种", name = "sellCoinId", required = false)
    private String sellCoinId;
    @ApiModelProperty(value = "交易市场", name = "marketName", required = false)
    private String marketName;
    @ApiModelProperty(value = "交易量", name = "sumVolume", required = false)
    private String sumVolume;
    @ApiModelProperty(value = "交易笔数", name = "turnoverNum", required = false)
    private String turnoverNum;
    @ApiModelProperty(value = "持币人数：是指持btc的人数（btc/usdt）,", name = "coinNum", required = false)
    private String coinNum;
    @ApiModelProperty(value = "最多交易用户", name = "coinNum", required = false)
    private String turnoverUserId;
    @ApiModelProperty(value = "最多交易用户的交易量:是指btc的交易量（btc/usdt）", name = "maxTurnoverNum", required = false)
    private String maxTurnoverNum;
    @ApiModelProperty(value = "充值时间", name = "created", required = false)
    private String created;

}
