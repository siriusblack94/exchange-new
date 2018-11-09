package com.blockeng.wallet.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/6/24 下午4:11
 * @Modified by: Chen Long
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CoinRechargeMessageDTO {

    /**
     * 充值用户
     */
    private Long userId;

    /**
     * 币种名称
     */
    private String coinName;

    /**
     * 充值数量
     */
    private BigDecimal amount;

    public CoinRechargeMessageDTO(Long userId, String coinName, BigDecimal amount) {
        this.userId = userId;
        this.coinName = coinName;
        this.amount = amount;
    }
}
