package com.blockeng.admin.entity;

import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 交易区
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Data
@Accessors(chain = true)
@TableName("trade_area")
public class TradeArea extends Model<TradeArea> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 交易区名称
     */
    @ApiModelProperty(value = "名称", name = "name", example = "", required = true)
    private String name;

    /**
     * 交易区代码
     */
    @ApiModelProperty(value = "代码", name = "code", example = "", required = true)
    private String code;

    /**
     * 类型：1-数字货币交易；2-创新交易使用；
     */
    @ApiModelProperty(value = "类型", name = "type", example = "1 币币交易 2 创新交易", required = true)
    private Integer type;

    /**
     * 结算币种（仅创新交易需要使用）
     */
    @TableField("coin_id")
    @ApiModelProperty(value = "结算币种ID", name = "coinId", example = "", required = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long coinId;

    /**
     * 结算币种名称（仅创新交易需要使用）
     */
    @TableField("coin_name")
    @ApiModelProperty(value = "结算币种名称", name = "coinName", example = "", required = false)
    private String coinName;

    /**
     * 排序
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Integer sort;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态", name = "status", example = "0 禁用 1启用", required = true)
    private Integer status;

    /**
     * 是否作为基础结算货币,0否1是 供统计个人账户使用
     */
    @TableField("base_coin")
    @ApiModelProperty(value = "资产统计标识", name = "baseCoin", example = "0 否 1 是", required = true)
    private Long baseCoin;

    /**
     * 修改时间
     */
    @TableField("last_update_time")
    private Date lastUpdateTime;

    /**
     * 创建时间
     */
    private Date created;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
