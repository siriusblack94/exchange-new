package com.blockeng.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/5/20 上午11:45
 * @Modified by: Chen Long
 */
@Data
@Accessors(chain = true)
@TableName("user_favorite_market")
public class UserFavoriteMarket {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @TableField("type")
    private Integer type;

    /**
     * 用户ID
     */
    @TableField("user_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /**
     * 交易对ID
     */
    @TableField("market_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long marketId;
}
