package com.blockeng.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @Description: 用户交易对资产
 * @Author: Chen Long
 * @Date: Created in 2018/5/15 上午11:55
 * @Modified by: Chen Long
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class SymbolAssetDTO {

    /**
     * 报价货币余额
     */
    private BigDecimal buyAmount;

    /**
     * 报价货币单位
     */
    private String buyUnit;

    /**
     * 报价货币冻结额度
     */
    private BigDecimal buyLockAmount;

    /**
     * 买方手续费费率
     */
    private BigDecimal buyFeeRate;

    /**
     * 基础货币余额
     */
    private BigDecimal sellAmount;

    /**
     * 基础货币单位
     */
    private String sellUnit;

    /**
     * 基础货币冻结额度
     */
    private BigDecimal sellLockAmount;

    /**
     * 卖方手续费费率
     */
    private BigDecimal sellFeeRate;
}
