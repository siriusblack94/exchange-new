package com.blockeng.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 注册，推荐奖励规则
 * </p>
 *
 * @author shaodw
 * @since 2018-09-18
 */
@Data
@Accessors(chain = true)
@TableName(value = "reward_config")
public class RewardConfig {

    @TableId(value = "id", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 币种id
     */
    @TableField("coin_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long coinId;

    /**
     * 币种name
     */
    @TableField("coin_name")
    @JsonSerialize(using = ToStringSerializer.class)
    private String coinName;

    /**
     * 奖励类型
     */
    @TableField("type")
    @JsonSerialize(using = ToStringSerializer.class)
    private String type;

    /**
     * 奖励开关
     */
    @TableField("status")
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer status;

    /**
     * 奖励数量
     */
    @TableField("amount")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal amount;

    /**
     * 开始日期
     */
    @TableField("start_time")
    @JsonSerialize(using = ToStringSerializer.class)
    private Date startTime;

    /**
     * 开始日期
     */
    @TableField("end_time")
    @JsonSerialize(using = ToStringSerializer.class)
    private Date  endTime;
}
