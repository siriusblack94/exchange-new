package com.blockeng.mining.dto;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class PoolDividendRecordDTO {


    /**
     * 累计分红
     */
    private BigDecimal amount;


    /**
     * 解冻日期
     */
    @TableField("reward_date")
    private String rewardDate;


    private BigDecimal usdtAccount;

    private BigDecimal cnyAccount;

}

