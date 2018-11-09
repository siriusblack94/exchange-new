package com.blockeng.admin.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.blockeng.framework.enums.BusinessType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 资金账户流水
 * </p>
 *
 * @author qiang
 * @since 2018-05-16
 */
@Data
@Accessors(chain = true)
@TableName("account_detail")
public class AccountDetail extends Model<AccountDetail> {

    private static final long serialVersionUID = 1L;
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ID_WORKER)
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
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long coinId;
    /**
     * 账户id
     */
    @TableField("account_id")
    @ApiModelProperty(value = "账户id", name = "accountId", example = "", required = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long accountId;
    /**
     * 该笔流水资金关联方的账户id
     */
    @TableField("ref_account_id")
    @ApiModelProperty(value = "关联账户id", name = "refAccountId", example = "", required = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long refAccountId;
    /**
     * 订单ID
     */
    @TableField("order_id")
    @ApiModelProperty(value = "关联订单ID", name = "orderId", example = "", required = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orderId;
    /**
     * 入账为1，出账为2
     */
    @ApiModelProperty(value = "收付类型", name = "direction", example = "1 入账 2 出账", required = false)
    private Integer direction;
    /**
     * 业务类型:
     * 充值(recharge_into)
     * 提现审核通过(withdrawals_out)
     * 下单(order_create)
     * 成交(order_turnover)
     * 成交手续费(order_turnover_poundage)
     * 撤单(order_cancel)
     * 注册奖励(bonus_register)
     * 提币冻结解冻(withdrawals)
     * 充人民币(recharge)
     * 提币手续费(withdrawals_poundage)
     * 兑换(cny_btcx_exchange)
     * 奖励充值(bonus_into)
     * 奖励冻结(bonus_freeze)
     */
    @TableField("business_type")
    @ApiModelProperty(value = "业务类型", name = "businessType", example = "", required = false)
    private String businessType;

    /**
     * 业务类型字符串
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "业务类型显示字符串", name = "businessTypeStr", example = "", required = false)
    private String businessTypeStr;

    /**
     * 资产数量
     */
    @ApiModelProperty(value = "金额", name = "amount", example = "", required = false)
    private BigDecimal amount;

    /**
     * 资产数量
     */
    @ApiModelProperty(value = "手续费", name = "fee", example = "", required = false)
    private BigDecimal fee;


    /**
     * 流水状态：
     * 充值
     * 提现
     * 冻结
     * 解冻
     * 转出
     * 转入
     */
    @ApiModelProperty(value = "备注", name = "remark", example = "", required = false)
    private String remark;
    /**
     * 日期
     */
    @ApiModelProperty(value = "时间", name = "created", example = "", required = false)
    private Date created;

    /**
     * 用户名
     */
    @TableField(value = "user_name", exist = false)
    @ApiModelProperty(value = "用户名", name = "userName", example = "", required = false)
    private String userName;


    /**
     * 用户手机号
     */
    @TableField(value = "mobile", exist = false)
    private String mobile;

    /**
     * 币种名
     */
    @TableField(value = "coin_name", exist = false)
    @ApiModelProperty(value = "币种名", name = "coinName", example = "", required = false)
    private String coinName;

    /**
     * 收付类型字符串
     */
    @TableField(exist = false)
    private String directionStr;

    /**
     * 创建时间字符串(for导出)
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String createdStr;

    /**
     * ID字符串(for导出)
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String idStr;

    /**
     * 账户ID字符串(for导出)
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String accountIdStr;

    /**
     * 关联账户ID字符串(for导出)
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String refAccountIdStr;

    /**
     * 订单ID字符串(for导出)
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String orderIdStr;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public String getDirectionStr() {
        if (1 == this.getDirection()) {
            return "入账";
        }
        if (2 == this.getDirection()) {
            return "出账";
        }
        return "";
    }

    public void setDirectionStr(String directionStr) {
        this.directionStr = directionStr;
    }

    public String getCreatedStr() {
        if (null == this.getCreated()) {
            return "";
        }
        DateTime dateTime = new DateTime(this.getCreated());
        return dateTime.toString("yyyy-MM-dd HH:mm:ss");
    }

    public void setCreatedStr(String createdStr) {
        this.createdStr = createdStr;
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


    public String getAccountIdStr() {
        if (null == this.getAccountId()) {
            return "";
        }
        return "\t" + String.valueOf(this.getAccountId());
    }

    public void setAccountIdStr(String accountIdStr) {
        this.accountIdStr = accountIdStr;
    }

    public String getRefAccountIdStr() {
        if (null == this.getRefAccountId()) {
            return "";
        }
        return "\t" + String.valueOf(this.getRefAccountId());
    }

    public void setRefAccountIdStr(String refAccountIdStr) {
        this.refAccountIdStr = refAccountIdStr;
    }

    public String getOrderIdStr() {
        if (null == this.getOrderId()) {
            return "";
        }
        return "\t" + String.valueOf(this.getOrderId());
    }

    public void setOrderIdStr(String orderIdStr) {
        this.orderIdStr = orderIdStr;
    }

    public String getBusinessTypeStr() {
        return BusinessType.getDescByCode(this.getBusinessType());
    }

    public void setBusinessTypeStr(String businessTypeStr) {
        this.businessTypeStr = businessTypeStr;
    }

    public AccountDetail() {
    }

    public AccountDetail(Long userId,
                         Long coinId,
                         Long accountId,
                         Long refAccountId,
                         Long orderId,
                         Integer direction,
                         String businessType,
                         BigDecimal amount,
                         String remark) {
        this.userId = userId;
        this.coinId = coinId;
        this.accountId = accountId;
        this.refAccountId = refAccountId;
        this.orderId = orderId;
        this.direction = direction;
        this.businessType = businessType;
        this.amount = amount;
        this.remark = remark;
    }

    public AccountDetail(Long userId,
                         Long coinId,
                         Long accountId,
                         Long refAccountId,
                         Long orderId,
                         Integer direction,
                         String businessType,
                         BigDecimal amount,
                         BigDecimal fee,
                         String remark) {
        this.userId = userId;
        this.coinId = coinId;
        this.accountId = accountId;
        this.refAccountId = refAccountId;
        this.orderId = orderId;
        this.direction = direction;
        this.businessType = businessType;
        this.amount = amount;
        this.fee = fee;
        this.remark = remark;
    }
}
