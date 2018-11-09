package com.blockeng.admin.entity;

import com.baomidou.mybatisplus.enums.IdType;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.Version;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 币种配置信息
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Data
@Accessors(chain = true)
public class Coin extends Model<Coin> {


    private static final long serialVersionUID = 1L;

    /**
     * 币种ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER )
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 币种名称
     */
    @ApiModelProperty(value = "币种名称", name = "name", example = "", required = true)
    private String name;
    /**
     * 币种标题
     */
    @ApiModelProperty(value = "币种标题", name = "title", example = "", required = true)
    private String title;
    /**
     * 币种logo
     */
    @ApiModelProperty(value = "图片", name = "img", example = "", required = true)
    private String img;
    /**
     * xnb：人民币
     * default：比特币系列
     * ETH：以太坊
     * ethToken：以太坊代币
     */
    @ApiModelProperty(value = "类型", name = "type", example = "xnb 人民币,default 比特币系列,ETH 以太坊,ethToken以太坊代币", required = true)
    private String type;
    /**
     * rgb：认购币
     * qbb：钱包币
     */
    @ApiModelProperty(value = "钱包类型", name = "type", example = "rgb 认购币,qbb 钱包币", required = true)
    private String wallet;
    /**
     * 小数位数
     */
    @ApiModelProperty(value = "小数位数", name = "round", example = "", required = true)
    private Integer round;

    /**
     * 最小提现单位
     */
    @TableField("base_amount")
    @ApiModelProperty(value = "最小提现单位", name = "baseAmount", example = "", required = true)
    private BigDecimal baseAmount;

    /**
     * 单笔最小提现数量
     */
    @TableField("min_amount")
    @ApiModelProperty(value = "单笔最小提现额度", name = "minAmount", example = "", required = true)
    private BigDecimal minAmount;

    /**
     * 单笔最大提现数量
     */
    @TableField("max_amount")
    @ApiModelProperty(value = "单笔最大提现额度", name = "maxAmount", example = "", required = true)
    private BigDecimal maxAmount;

    /**
     * 当日最大提现数量
     */
    @TableField("day_max_amount")
    @ApiModelProperty(value = "当日最大提现额度", name = "dayMaxAmount", example = "", required = true)
    private BigDecimal dayMaxAmount;

    /**
     * status=1：启用
     * 0：禁用
     */
    @ApiModelProperty(value = "状态", name = "status", example = "0 禁用 1启用", required = true)
    private Integer status;
    /**
     * 自动转出数量
     */
    @TableField("auto_out")
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Double autoOut;
    /**
     * 手续费率
     */
    @ApiModelProperty(value = "手续费率", name = "rate", example = "2.2", required = true)
    private Double rate;
    /**
     * 最低收取手续费个数
     */
    @TableField("min_fee_num")
    @ApiModelProperty(value = "最低提现手续费", name = "minFeeNum", example = "", required = true)
    private BigDecimal minFeeNum;
    /**
     * 提现开关
     */
    @TableField("withdraw_flag")
    @ApiModelProperty(value = "提现状态", name = "withdrawFlag", example = "0 关闭 1开启", required = true)
    private Integer withdrawFlag;
    /**
     * 充值开关
     */
    @TableField("recharge_flag")
    @ApiModelProperty(value = "充值状态", name = "rechargeFlag", example = "0 关闭 1开启", required = true)
    private Integer rechargeFlag;
    /**
     * 更新时间
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
