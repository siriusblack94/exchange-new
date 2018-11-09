package com.blockeng.task.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/4/18 下午2:22
 * @Modified by: Chen Long
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class MarketDataDTO implements java.io.Serializable {

    /**
     * 交易对ID
     */
    private int marketId;

    /**
     * 交易对名称
     */
    private String market;

    /**
     * 涨跌幅（成交后需要更新）
     */
    private BigDecimal change;

    /**
     * 点差（委托下单后需要更新，修改market后需要更新）
     */
    private BigDecimal spread;

    /**
     * 最新成交价（成交后需要更新）
     */
    private BigDecimal price;

    /**
     * 买一价（委托下单后需要更新）
     */
    private BigDecimal buyPrice;

    /**
     * 卖一价（委托下单后需要更新）
     */
    private BigDecimal sellPrice;

    /**
     * （委托下单后需要更新，修改market后需要更新）
     * 计算保证金基础数据
     */
    private BigDecimal baseMargin;
}
