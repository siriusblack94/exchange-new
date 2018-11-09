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
@TableName("asset_snapshot_detail")
public class AssetSnapshotDetail {

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
     * 用户ID
     */
    @TableField("coin_id")
    private Long coinId;


    /**
     * 账户资产
     */
    @TableField("balance_amount")
    private BigDecimal balanceAmount;
    /**
     * 账户资产
     */
    @TableField("freeze_amount")
    private BigDecimal freezeAmount;


    /**
     * 资产对应usdt的价格
     */
    private BigDecimal balance;


    /**
     * 现价
     */
    private BigDecimal price;


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
