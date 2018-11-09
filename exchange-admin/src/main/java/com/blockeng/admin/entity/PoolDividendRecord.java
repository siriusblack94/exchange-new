package com.blockeng.admin.entity;



import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
@TableName("pool_dividend_record")
public class PoolDividendRecord {

    @TableId("id")
    private Long id;

    @TableField("user_id")
    private Long userId;


    @TableField("amount")
    private BigDecimal amount=BigDecimal.ZERO;
    /**
     * 分红日期
     */
    @TableField("reward_date")
    private String rewardDate;

    /**
     * 备注
     */
    @TableField("mark")
    private String mark;


    private Date created;

}

