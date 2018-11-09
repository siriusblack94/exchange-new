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
@TableName("mine")
public class Mine {

    @TableId("id")
    private Long id;

    @TableField("user_id")
    private Long userId;

    /**
     * 交易总额度
     */
    @TableField("total_mining")
    private BigDecimal totalMining;

    /**
     * 交易总额度
     */
    @TableField("real_mining")
    private BigDecimal realMining;

    @TableField("time_mining")
    private String timeMining;

    private Date created;
    /**
     * 更新时间
     */
    @TableField("last_update_time")
    private Date lastUpdateTime;
}
