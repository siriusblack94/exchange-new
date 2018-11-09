package com.blockeng.mining.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: sirius
 * @Date: 2018年8月15日13:04:22
 * @Description:私募类
 */


@Data
@Accessors(chain = true)
@TableName("private_placement")
public class PrivatePlacement {
  /**
   * 描述: 主键
   * @auther: sirius
   * @date: 2018年8月15日13:09:20
   */
    @TableId("id")
    private Long id;

    @TableField("user_id")
    private Long userId;

    /**
     * 描述: 私募金额
     * @auther: sirius
     * @date: 2018年8月15日13:09:20
     */
    @TableField("amount")
    private BigDecimal amount;


    /**
     * 描述: 私募冻结金额
     * @auther: sirius
     * @date: 2018年8月15日13:09:20
     */
    @TableField("freeze_amount")
    private BigDecimal freezeAmount;


    /**
     * 描述: 私募释放金额
     * @auther: sirius
     * @date: 2018年8月15日13:09:20
     */
    @TableField("release_amount")
    private BigDecimal releaseAmount;

    @TableField("created")
    private Date created;

    @TableField("last_update_time")
    private Date lastUpdateTime;



}
