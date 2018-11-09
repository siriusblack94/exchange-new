package com.blockeng.mining.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * 用户财产记录
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Data
@Accessors(chain = true)
@TableName(value = "account")
public class Account extends Model<Account> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 用户id
     */
    @TableField("user_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    /**
     * 币种id
     */
    @TableField("coin_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long coinId;
    /**
     * 账号状态：1，正常；2，冻结；
     */
    private Integer status;
    /**
     * 币种可用金额
     */
    @TableField("balance_amount")
    private BigDecimal balanceAmount;
    /**
     * 币种冻结金额
     */
    @TableField("freeze_amount")
    private BigDecimal freezeAmount;
    /**
     * 累计充值金额
     */
    @TableField("recharge_amount")
    private BigDecimal rechargeAmount;
    /**
     * 累计提现金额
     */
    @TableField("withdrawals_amount")
    private BigDecimal withdrawalsAmount;
    /**
     * 净值
     */
    @TableField("net_value")
    private BigDecimal netValue;
    /**
     * 占用保证金
     */
    @TableField("lock_margin")
    private BigDecimal lockMargin;
    /**
     * 持仓盈亏/浮动盈亏
     */
    @TableField("float_profit")
    private BigDecimal floatProfit;
    /**
     * 总盈亏
     */
    @TableField("total_profit")
    private BigDecimal totalProfit;
    /**
     * 充值地址
     */
    @TableField("rec_addr")
    private String recAddr;
    /**
     * 版本号
     */
    private Long version;
    /**
     * 更新时间
     */
    @TableField("last_update_time")
    private Date lastUpdateTime;
    /**
     * 创建时间
     */
    private Date created;

    public Account() {
    }

    public Account(Long userId, Long coinId, Integer status, BigDecimal balanceAmount, BigDecimal freezeAmount, BigDecimal rechargeAmount, BigDecimal withdrawalsAmount, BigDecimal netValue, BigDecimal lockMargin, BigDecimal floatProfit, BigDecimal totalProfit, String recAddr, Long version, Date lastUpdateTime, Date created) {
        this.userId = userId;
        this.coinId = coinId;
        this.status = status;
        this.balanceAmount = balanceAmount;
        this.freezeAmount = freezeAmount;
        this.rechargeAmount = rechargeAmount;
        this.withdrawalsAmount = withdrawalsAmount;
        this.netValue = netValue;
        this.lockMargin = lockMargin;
        this.floatProfit = floatProfit;
        this.totalProfit = totalProfit;
        this.recAddr = recAddr;
        this.version = version;
        this.lastUpdateTime = lastUpdateTime;
        this.created = created;
    }

    public Account(Long id, Long userId, Long coinId, Integer status, BigDecimal balanceAmount, BigDecimal freezeAmount, BigDecimal rechargeAmount, BigDecimal withdrawalsAmount, BigDecimal netValue, BigDecimal lockMargin, BigDecimal floatProfit, BigDecimal totalProfit, String recAddr, Long version, Date lastUpdateTime, Date created) {
        this.id = id;
        this.userId = userId;
        this.coinId = coinId;
        this.status = status;
        this.balanceAmount = balanceAmount;
        this.freezeAmount = freezeAmount;
        this.rechargeAmount = rechargeAmount;
        this.withdrawalsAmount = withdrawalsAmount;
        this.netValue = netValue;
        this.lockMargin = lockMargin;
        this.floatProfit = floatProfit;
        this.totalProfit = totalProfit;
        this.recAddr = recAddr;
        this.version = version;
        this.lastUpdateTime = lastUpdateTime;
        this.created = created;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
