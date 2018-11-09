package com.blockeng.mining.entity;

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
 * @Description: 资产快照
 * @Author: Chen Long
 * @Date: Created in 2018/6/30 上午12:05
 * @Modified by: Chen Long
 */
@Data
@Accessors(chain = true)
@TableName("asset_snapshot")
public class AssetSnapshot {

    /**
     * 主键
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 账户资产
     */
    @TableField("balance")
    private BigDecimal balance;

    /**
     * 账户资产
     */
    @TableField("snap_time")
    private String snapTime;

    /**
     * 快照时间
     */
    private String date;

    /**
     * 创建时间
     */
    private Date created;

}
