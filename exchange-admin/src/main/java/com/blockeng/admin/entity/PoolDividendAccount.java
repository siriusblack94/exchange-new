package com.blockeng.admin.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
@TableName("pool_dividend_account")
public class PoolDividendAccount {


    @TableId("id")
    private Long id;

    @TableField("user_id")
    private Long userId;

    /**
     * 累计分红
     */
    @TableField("reward_amount")
    private BigDecimal rewardAmount=BigDecimal.ZERO;

    /**
     * 已解冻
     */
    @TableField("unlock_amount")
    private BigDecimal unlockAmount=BigDecimal.ZERO;

    /**
     * 未解冻
     */
    @TableField("lock_amount")
    private BigDecimal lockAmount=BigDecimal.ZERO;


    @TableField("pri_month_total_mine")

    private BigDecimal priMonthTotalMine=BigDecimal.ZERO;
    /**
     * 解冻日期
     */
    @TableField("unlock_date")
    private String unlockDate;


    private Date created;

    /**
     * 更新时间
     */
    @TableField("last_update_time")
    private Date lastUpdateTime;

}

