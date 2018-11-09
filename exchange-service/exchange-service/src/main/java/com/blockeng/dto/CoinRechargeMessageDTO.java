package com.blockeng.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @Description: 充币结果消息
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
     * 交易ID
     */
    private String txid;

    /**
     * 充值地址
     */
    private String address;

    /**
     * 充值数量
     */
    private BigDecimal amount;
}
