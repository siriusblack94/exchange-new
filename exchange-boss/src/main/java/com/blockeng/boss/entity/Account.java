package com.blockeng.boss.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 用户财产记录
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Account {

    /**
     * 自增id
     */
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
     * 账号状态：1，正常；2，冻结；
     */
    private Integer status;

    /**
     * 币种可用金额
     */
    private BigDecimal balanceAmount;

    /**
     * 币种冻结金额
     */
    private BigDecimal freezeAmount;
}
