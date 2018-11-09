package com.blockeng.entity;

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
 * 法币提现表
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Data
@Accessors(chain = true)
@TableName("cash_withdrawals")
public class CashWithdrawals extends Model<CashWithdrawals> {

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
     * 币种ID
     */
    @TableField("coin_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long coinId;

    /**
     * 资金账户ID
     */
    @TableField("account_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long accountId;

    /**
     * 数量（提现金额）
     */
    private BigDecimal num;

    /**
     * 手续费
     */
    private BigDecimal fee;

    /**
     * 到账金额
     */
    private BigDecimal mum;
    /**
     * 开户人
     */
    private String truename;

    /**
     * 银行名称
     */
    private String bank;

    /**
     * 银行所在省
     */
    @TableField("bank_prov")
    private String bankProv;

    /**
     * 银行所在市
     */
    @TableField("bank_city")
    private String bankCity;

    /**
     * 开户行
     */
    @TableField("bank_addr")
    private String bankAddr;

    /**
     * 银行账号
     */
    @TableField("bank_card")
    private String bankCard;

    /**
     * 审核备注
     */
    private String remark;

    /**
     * 当前审核级数
     */
    private Integer step;

    /**
     * 状态：0-待审核；1-审核通过；2-拒绝；3-提现成功；
     */
    private Integer status;

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
