package com.blockeng.admin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/6/29 下午3:37
 * @Modified by: Chen Long
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("mine_data")
public class MineData {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 昨日待分配累积折合
     */
    @TableField("pre_distributed")
    private BigDecimal preDistributed;

    /**
     * 前一日挖矿产出
     */
    @TableField("pre_mining")
    private BigDecimal preMining;

    /**
     * 总流通量
     */
    @TableField("total_circulation")
    private BigDecimal totalCirculation;

    /**
     * 二级市场流通量
     */
    @TableField("sec_irculation")
    private BigDecimal secIrculation;

    /**
     * 今日待分配累积折合
     */
    private BigDecimal distributed;

    /**
     * 今日连续持有每百万分收入
     */
    @TableField("per_million_revenue")
    private BigDecimal perMillionRevenue;

    /**
     * 统计日期
     */
    @TableField("statistics_date")
    private String statisticsDate;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 更新时间
     */
    @TableField("last_update_time")
    private Date lastUpdateTime;

}

