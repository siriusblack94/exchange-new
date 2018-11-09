package com.blockeng.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/6/11 下午12:14
 * @Modified by: Chen Long
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class DealRecordDTO {

    /**
     * 市场名称
     */
    private String marketName;

    /**
     * 类型 交易类型:1 买 2卖 :trade_type
     */
    private int type;

    /**
     * 成交价 price
     */
    private BigDecimal turnoverPrice;

    /**
     * 成交数量  volume
     */
    private BigDecimal turnoverNum;

    /**
     * 成交额  总额  amount
     */
    private BigDecimal turnoverAmount;

    /**
     * 手续费 成交手续费
     */
    private BigDecimal dealSellFee;

    /**
     * 手续费 成交手续费
     */
    private BigDecimal dealBuyFee;

    /**
     * 手续费
     */
    private BigDecimal fee;

    /**
     * 成交时间
     */
    private Date createTime;
}
