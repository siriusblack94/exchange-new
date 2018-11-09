package com.blockeng.admin.entity;

import com.baomidou.mybatisplus.enums.IdType;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户财产记录
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Data
@Accessors(chain = true)
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
    @ApiModelProperty(value = "用户id", name = "userId", example = "", required = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    /**
     * 币种id
     */
    @TableField("coin_id")
    @ApiModelProperty(value = "币种id", name = "coinId", example = "", required = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long coinId;
    /**
     * 账号状态：1，正常；2，冻结；
     */
    @ApiModelProperty(value = "账号状态", name = "status", example = "1 正常 2 冻结", required = false)
    private Integer status;
    /**
     * 币种可用金额
     */
    @TableField("balance_amount")
    @ApiModelProperty(value = "可用金额", name = "balanceAmount", example = "", required = false)
    private BigDecimal balanceAmount;

    /**
     * 币种冻结金额
     */
    @TableField("freeze_amount")
    @ApiModelProperty(value = "冻结金额", name = "freezeAmount", example = "", required = false)
    private BigDecimal freezeAmount;
    /**
     * 累计充值金额
     */
    @TableField("recharge_amount")
    @ApiModelProperty(value = "累计充值金额", name = "rechargeAmount", example = "", required = false)
    //@JsonIgnore
    //@ApiModelProperty(hidden = true)
    private BigDecimal rechargeAmount;
    /**
     * 累计提现金额
     */
    @TableField("withdrawals_amount")
    @ApiModelProperty(value = "累计提现金额", name = "withdrawalsAmount", example = "", required = false)
    //@JsonIgnore
    //@ApiModelProperty(hidden = true)
    private BigDecimal withdrawalsAmount;
    /**
     * 净值
     */
    @TableField("net_value")
    //@ApiModelProperty(value="净值",name="netValue",example="",required=false)
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private BigDecimal netValue;
    /**
     * 占用保证金
     */
    @TableField("lock_margin")
    //@ApiModelProperty(value="占用保证金",name="lockMargin",example="",required=false)
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private BigDecimal lockMargin;
    /**
     * 持仓盈亏/浮动盈亏
     */
    @TableField("float_profit")
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private BigDecimal floatProfit;
    /**
     * 总盈亏
     */
    @TableField("total_profit")
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private BigDecimal totalProfit;
    /**
     * 充值地址
     */
    @TableField("rec_addr")
    @ApiModelProperty(value = "钱包地址", name = "recAddr", example = "", required = false)
    private String recAddr;
    /**
     * 版本号
     */
    @Version
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

    /**
     * 用户名
     */
    @TableField(value = "userName", exist = false)
    @ApiModelProperty(value = "用户名", name = "userName", example = "", required = false)
    private String userName;

    /**
     * 用户真实名
     */
    @TableField(value = "realName", exist = false)
    private String realName;

    /**
     * 用户手机号
     */
    @TableField(value = "mobile", exist = false)
    private String mobile;

    /**
     * 币种名
     */
    @TableField(value = "coinName", exist = false)
    private String coinName;

    /**
     * 状态显示字符串：1，正常；2，冻结
     */
    @TableField(exist = false)
    private String statusStr;

    /**
     * ID字符串(for导出)
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String idStr;

    /**
     * 余额字符串(for导出)
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String balanceAmountStr;

    /**
     * 冻结字符串(for导出)
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String freezeAmountStr;

    /**
     * 累计充值字符串(for导出)
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String rechargeAmountStr;

    /**
     * 累计提现字符串(for导出)
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String withdrawalsAmountStr;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public String getStatusStr() {
        if (this.getStatus() != null) {
            if (1 == this.getStatus()) {
                return "正常";
            }
            if (2 == this.getStatus()) {
                return "冻结";
            }
        }

        return "";
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public String getIdStr() {
        if (null == this.getId()) {
            return "";
        }
        //导出ID过长
        return "\t" + String.valueOf(this.getId());
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public String getBalanceAmountStr() {
        if (this.getBalanceAmount() == null || 0 == this.getBalanceAmount().compareTo(BigDecimal.ZERO)) {
            return " 0 ";
        }
        return "\t" + String.valueOf(this.getBalanceAmount());
    }

    public void setBalanceAmountStr(String balanceAmountStr) {
        this.balanceAmountStr = balanceAmountStr;
    }

    public String getFreezeAmountStr() {
        if (this.getFreezeAmount() == null || 0 == this.getFreezeAmount().compareTo(BigDecimal.ZERO)) {
            return " 0 ";
        }
        return "\t" + String.valueOf(this.getFreezeAmount());
    }

    public void setFreezeAmountStr(String freezeAmountStr) {
        this.freezeAmountStr = freezeAmountStr;
    }

    public String getRechargeAmountStr() {
        if (this.getFreezeAmount() == null || 0 == this.getRechargeAmount().compareTo(BigDecimal.ZERO)) {
            return " 0 ";
        }
        return "\t" + String.valueOf(this.getRechargeAmount());
    }

    public void setRechargeAmountStr(String rechargeAmountStr) {
        this.rechargeAmountStr = rechargeAmountStr;
    }

    public String getWithdrawalsAmountStr() {
        if (this.getFreezeAmount() == null || 0 == this.getWithdrawalsAmount().compareTo(BigDecimal.ZERO)) {
            return " 0 ";
        }
        return "\t" + String.valueOf(this.getWithdrawalsAmount());
    }

    public void setWithdrawalsAmountStr(String withdrawalsAmountStr) {
        this.withdrawalsAmountStr = withdrawalsAmountStr;
    }
}
