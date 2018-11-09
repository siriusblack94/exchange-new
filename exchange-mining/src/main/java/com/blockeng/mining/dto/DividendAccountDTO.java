package com.blockeng.mining.dto;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
public class DividendAccountDTO {


    /**
     * 本周累计分红
     */
    @TableField("reward_amount")
    private BigDecimal rewardAmount;

    /**
     * 已解冻
     */
    @TableField("unlock_amount")
    private BigDecimal unlockAmount;

    /**
     * 未解冻
     */
    @TableField("lock_amount")
    private BigDecimal lockAmount;

    /**
     * 解冻日期
     */
    @TableField("unlock_date")
    private String unlockDate;

    private BigDecimal usdtAccount;

    private BigDecimal cnyAccount;
}
