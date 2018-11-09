package com.blockeng.admin.dto;


import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.entity.DigitalCoinRechargeStatistics;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
/**
 * 数字币充值统计 新
 * */
public class DigitalCoinRechargeStatisticsDTO extends PageDTO{

    @ApiModelProperty(value = "币种", name = "coinName", required = false)
    private String coinName="";

    @ApiModelProperty(value = "充值数量", name = "rechargeCount", required = false)
    private BigDecimal rechargeCount=new BigDecimal(0);

    @ApiModelProperty(value = "充值方式", name = "rechargeMethod", required = false)
    private String rechargeMethod="";

    @ApiModelProperty(value = "充值次数", name = "rechargeTimes", required = false)
    private Integer rechargeTimes=0;

    public DigitalCoinRechargeStatisticsDTO(){
        setRecords(new ArrayList());
    }

}
