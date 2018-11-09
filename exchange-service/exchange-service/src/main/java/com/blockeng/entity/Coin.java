package com.blockeng.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 币种配置信息
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Data
@Accessors(chain = true)
public class Coin extends Model<Coin> {

    private static final long serialVersionUID = 1L;

    /**
     * 币种ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 币种名称
     */
    private String name;
    /**
     * 币种标题
     */
    private String title;
    /**
     * 币种logo
     */
    private String img;
    /**
     * xnb：人民币
     * default：比特币系列
     * ETH：以太坊
     * ethToken：以太坊代币
     */
    private String type;
    /**
     * rgb：认购币
     * qbb：钱包币
     */
    private String wallet;
    /**
     * 小数位数
     */
    private Integer round;
    /**
     * status=1：启用
     * 0：禁用
     */
    private Integer status;
    /**
     * 自动转出数量
     */
    @TableField("auto_out")
    private Double autoOut;
    /**
     * 手续费率
     */
    private Double rate;
    /**
     * 最低收取手续费个数
     */
    @TableField("min_fee_num")
    private BigDecimal minFeeNum;


    /**
     * base_amount 最小提现单位
     */
    @TableField("base_amount")
    private BigDecimal baseAmount;

    /**
     * min_amount  单笔最小提现数量
     */
    @TableField("min_amount")
    private BigDecimal minAmount;

    /**
     * max_amount 单笔最大提现数量
     */
    @TableField("max_amount")
    private BigDecimal maxAmount;


    /**
     * day_max_amount  当日最大提现数量
     */
    @TableField("day_max_amount")
    private BigDecimal dayMaxAmount;

    /**
     * 提现开关
     */
    @TableField("withdraw_flag")
    private Integer withdrawFlag;
    /**
     * 充值开关
     */
    @TableField("recharge_flag")
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
