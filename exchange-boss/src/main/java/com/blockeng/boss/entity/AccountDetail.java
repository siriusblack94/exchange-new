package com.blockeng.boss.entity;

import lombok.Data;
import lombok.experimental.Accessors;

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
public class AccountDetail {

    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 币种id
     */
    private Long coinId;

    /**
     * 账户id
     */
    private Long accountId;

    /**
     * 该笔流水资金关联方的账户id
     */
    private Long refAccountId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 入账为1，出账为2
     */
    private Integer direction;

    /**
     * 业务类型:
     */
    private String businessType;

    /**
     * 资产数量
     */
    private BigDecimal amount;

    /**
     * 手续费
     */
    private BigDecimal fee;

    /**
     * 流水状态：
     */
    private String remark;

    /**
     * 日期
     */
    private Date created;

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
