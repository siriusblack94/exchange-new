package com.blockeng.mining.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class PlantCoinDividendTotalDTO {
    /**
     * 未解冻
     */
    private BigDecimal amount;

    private String coinName;

    private BigDecimal usdtAccount;

    private BigDecimal cnyAccount;
}
