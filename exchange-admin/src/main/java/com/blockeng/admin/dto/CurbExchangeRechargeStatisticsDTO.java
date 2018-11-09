package com.blockeng.admin.dto;


import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.entity.CurbExchangeRechargeStatistics;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
/**
 * 场外交易 充值统计 新
 * */
public class CurbExchangeRechargeStatisticsDTO extends PageDTO{

    @ApiModelProperty(value = "充值金额", name = "rechageAmount", required = false)
    private BigDecimal rechageAmount;

    @ApiModelProperty(value = "到账金额", name = "transferAmount", required = false)
    private BigDecimal transferAmount;

    @ApiModelProperty(value = "充值次数", name = "rechargeTimes", required = false)
    private Integer rechargeTimes;


    public CurbExchangeRechargeStatisticsDTO(){
        setRecords(new ArrayList());
    }

}
