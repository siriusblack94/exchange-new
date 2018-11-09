package com.blockeng.mining.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 解冻,未解冻,锁仓总和
 */
@Data
@Accessors(chain = true)
public class PoolDividendTotalAccountDTO {


    /**
     * 累计分红
     */
    private BigDecimal totalRewardAmount;

    /**
     * 已解冻
     */
    private BigDecimal totalUnlockAmount;

    /**
     * 未解冻
     */
    private BigDecimal totalLockAmount;

    /**
     * 未解冻
     */
    private BigDecimal cnyAmount;

    /**
     * 未解冻
     */
    private BigDecimal usdtAmount;


    /**
     * 未解冻
     */
    private BigDecimal totalHold;
}
