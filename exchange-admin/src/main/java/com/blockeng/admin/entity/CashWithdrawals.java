package com.blockeng.admin.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 法币提现表
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Data
@Accessors(chain = true)
@TableName("cash_withdrawals")
public class CashWithdrawals extends Model<CashWithdrawals> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ID_WORKER)
    @ApiModelProperty(value = "主键id", name = "id")
    private Long id;

    /**
     * 用户id
     */
    @TableField("user_id")
    @ApiModelProperty(value = "用户id", name = "userId", required = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /**
     * 币种ID
     */
    @TableField("coin_id")
    @ApiModelProperty(value = "币种id", name = "coinId", required = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long coinId;

    /**
     * 资金账户ID
     */
    @TableField("account_id")
    @ApiModelProperty(value = "用户财产记录id", name = "accountId", required = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long accountId;

    /**
     * 数量（提现金额）
     */
    @ApiModelProperty(value = "数量（提现金额）", name = "num", required = true)
    private BigDecimal num;

    /**
     * 手续费
     */
    @ApiModelProperty(value = "手续费", name = "fee", required = true)
    private BigDecimal fee;

    /**
     * 到账金额
     */
    @ApiModelProperty(value = "到账金额", name = "mum", required = true)
    private BigDecimal mum;

    /**
     * 开户人
     */
    @ApiModelProperty(value = "开户人", name = "truename", required = true)
    private String truename;

    /**
     * 银行名称
     */
    @ApiModelProperty(value = "银行名称", name = "bank", required = true)
    private String bank;

    /**
     * 银行所在省
     */
    @ApiModelProperty(value = "银行所在省", name = "bankProv")
    @TableField("bank_prov")
    private String bankProv;

    /**
     * 银行所在市
     */
    @TableField("bank_city")
    @ApiModelProperty(value = "银行所在市", name = "bankCity")
    private String bankCity;

    /**
     * 开户行
     */
    @TableField("bank_addr")
    @ApiModelProperty(value = "开户行", name = "bankAddr")
    private String bankAddr;

    /**
     * 银行账号
     */
    @TableField("bank_card")
    @ApiModelProperty(value = "银行账号", name = "bankCard", required = true)
    private String bankCard;

    /**
     * 审核备注
     */
    @ApiModelProperty(value = "备注", name = "remark")
    private String remark;

    /**
     * 当前审核级数
     */
    @ApiModelProperty(value = "当前审核级数", name = "step")
    private Integer step;

    /**
     * 状态：0-待审核；1-审核通过；2-拒绝；3-提现成功；
     */
    @ApiModelProperty(value = "状态：0-待审核；1-审核通过；2-拒绝；3-提现成功；", name = "status", required = true)
    private Integer status;

    /**
     * 更新时间
     */
    @TableField("last_update_time")
    @ApiModelProperty(value = "更新时间", name = "lastUpdateTime")
    private Date lastUpdateTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", name = "created")
    private Date created;

    @ApiModelProperty(value = "最后确认提现到账时间", name = "lastTime", required = true)
    @TableField("last_time")
    private Date lastTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
