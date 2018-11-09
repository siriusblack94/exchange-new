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
@TableName("dividend_record")
public class DividendRecord {

    @TableId("id")
    private Long id;

    @TableField("user_id")
    private Long userId;

    /**
     * 关联用户id
     */
    @TableField("refe_user_id")
    private Long refeUserId;


    @TableField("scale_amount")
    private String scaleAmount;


    @TableField("invite_amount")
    private BigDecimal inviteAmount;

    /**
     * 分红日期
     */
    @TableField("reward_date")
    private String rewardDate;

    /**
     * 1可用,0不可用(无效冻结金额)
     */
    @TableField("enable")
    private Integer enable;

    private String mark;

    private Date created;

}

