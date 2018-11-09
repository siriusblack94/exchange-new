package com.blockeng.mining.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
@TableName("plant_coin_dividend_account")
public class PlantCoinDividendAccount {

    @TableId("id")
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("coin_name")
    private Long coinName;

    /**
     * 累计分红
     */
    @TableField("reward_amount")
    private BigDecimal rewardAmount;

    /**
     * 已解冻
     */
    @TableField("unlock_amount")
    private String unlockAmount;

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
