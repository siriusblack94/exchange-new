package com.blockeng.admin.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @Auther: sirius
 * @Date: 2018/8/27 22:05
 * @Description:
 */
@Data
@Accessors(chain = true)
@TableName("dividend_release_record")
public class DividendReleaseRecord {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    /**
     * 本周累计分红
     */
    @TableField("week_lock")
    private BigDecimal weekLock=BigDecimal.ZERO;


    @TableField("pri_week_total_mine")
    private BigDecimal priWeekTotalMine=BigDecimal.ZERO;

    @TableField("unlock_date")
    private String unlockDate;

    /**
     * 解冻数量
     */
    @TableField("unlock_amount")
    private BigDecimal unlockAmount=BigDecimal.ZERO;

}
