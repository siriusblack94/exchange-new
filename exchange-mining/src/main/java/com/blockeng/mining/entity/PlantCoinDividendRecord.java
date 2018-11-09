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
@TableName("plant_coin_dividend_record")
public class PlantCoinDividendRecord {

    @TableId("id")
    private Long id;

    @TableField("user_id")
    private Long userId;
    /**
     * 币种名称
     */
    @TableField("coin_name")
    private String coinName;

    private BigDecimal amount;
    /**
     * 分红日期
     */
    @TableField("reward_date")
    private String rewardDate;

    private Date created;

}

