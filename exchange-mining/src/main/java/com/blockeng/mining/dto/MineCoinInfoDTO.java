package com.blockeng.mining.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class MineCoinInfoDTO {


    /**
     * 未解冻
     */
    private BigDecimal balanceAmount;

    private BigDecimal freezeAmount;

    private BigDecimal amount;

    private BigDecimal usdtAccount;

    private BigDecimal cnyAccount;
}
