package com.blockeng.admin.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
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
@Accessors(chain = true)
public class MineDataDTO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "主键id", name = "id")
    private Long id;

    /**
     * 昨日待分配累积折合
     */
    @TableField("pre_distributed")
    @ApiModelProperty(value = "昨日待分配累积折合", name = "preDistributed", required = false)
    private BigDecimal preDistributed;

    /**
     * 前一日挖矿产出
     */
    @TableField("pre_mining")
    @ApiModelProperty(value = "前一日挖矿产出", name = "preDistributed", required = false)
    private BigDecimal preMining;

    /**
     * 总流通量
     */
    @TableField("total_circulation")
    @ApiModelProperty(value = "总流通量", name = "totalCirculation", required = false)
    private BigDecimal totalCirculation;

    /**
     * 二级市场流通量
     */
    @TableField("sec_irculation")
    @ApiModelProperty(value = "二级市场流通量", name = "secIrculation", required = false)
    private BigDecimal secIrculation;

    /**
     * 今日待分配累积折合
     */
    @ApiModelProperty(value = "今日待分配累积折合", name = "distributed", required = false)
    private BigDecimal distributed;

    /**
     * 今日连续持有每百万分收入
     */
    @TableField("per_million_revenue")
    @ApiModelProperty(value = "今日连续持有每百万分收入", name = "perMillionRevenue", required = false)
    private BigDecimal perMillionRevenue;

    /**
     * 统计日期
     */
    @TableField("statistics_date")
    @ApiModelProperty(value = "统计日期", name = "statisticsDate", required = false, example = "2018-07-10")
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
