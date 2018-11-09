package com.blockeng.admin.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: sirius
 * @Date: 2018年8月15日13:04:22
 * @Description:私募释放记录类
 */

@Data
@Accessors(chain = true)
@TableName("private_placement_release_record")
public class PrivatePlacementReleaseRecord {
    @TableId("id")
    private Long id;

    @TableField("user_id")
    private Long userId;

    /**
     * 描述: 私募释放金额
     * @auther: sirius
     * @date: 2018年8月15日13:09:20
     */
    @TableField("release_amount")
    private BigDecimal releaseAmount;
    
    /**
     * 描述: 私募释放比例
     * @auther: sirius
     * @date: 2018年8月15日13:09:20
     */
    @TableField("release_amount_rate")
    private BigDecimal releaseAmountRate;

    @TableField("created")
    private Date created;

}
