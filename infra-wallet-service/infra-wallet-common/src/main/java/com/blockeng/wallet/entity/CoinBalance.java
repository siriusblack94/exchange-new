package com.blockeng.wallet.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 币种余额
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Data
@Accessors(chain = true)
@TableName("coin_balance")
public class CoinBalance extends Model<CoinBalance> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 币种ID
     */
    @TableField("coin_id")
    private Long coinId;

    /**
     * 币种名称
     */
    @TableField("coin_name")
    private String coinName;

    /**
     * 系统余额（根据充值提币计算）
     */
    @TableField("system_balance")
    private BigDecimal systemBalance;

    /**
     * 币种类型
     */
    @TableField("coin_type")
    private String coinType;

    /**
     * 归集账户余额
     */
    @TableField("collect_account_balance")
    private BigDecimal collectAccountBalance;

    /**
     * 打币账户余额
     */
    @TableField("loan_account_balance")
    private BigDecimal loanAccountBalance;

    /**
     * 手续费账户余额
     */
    @TableField("fee_account_balance")
    private BigDecimal feeAccountBalance;

    /**
     * 手续费账户余额
     */
    @TableField("recharge_account_balance")
    private BigDecimal rechargeAccountBalance;


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
