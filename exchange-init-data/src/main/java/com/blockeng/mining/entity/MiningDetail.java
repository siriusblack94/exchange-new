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
@TableName("mining_detail")
public class MiningDetail {

    @TableId("id")
    private Long id;

    @TableField("user_id")
    private Long userId;

    /**
     * 交易区域名称
     */
    @TableField("area_name")
    private String areaName;

    /**
     * 交易总额度
     */
    @TableField("total_fee")
    private BigDecimal totalFee;

    @TableField("time_mining")
    private String timeMining;

    private Date created;
    /**
     * 更新时间
     */
    @TableField("last_update_time")
    private Date lastUpdateTime;
}
