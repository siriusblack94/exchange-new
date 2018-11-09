package com.blockeng.mining.dto;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ApiModel(value = "挖矿奖励", description = "挖矿奖励")
public class MineDecordDTO {

    private BigDecimal miningAccount;

    private BigDecimal mineUsdt;


    private BigDecimal mineCny;

    private String rewardDate;
}
