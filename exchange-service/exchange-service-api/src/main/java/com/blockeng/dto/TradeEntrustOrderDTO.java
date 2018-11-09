package com.blockeng.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 未完成委托订单
 * @Author: Chen Long
 * @Date: Created in 2018/5/14 下午8:43
 * @Modified by: Chen Long
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class TradeEntrustOrderDTO {

    /**
     * 订单号
     */
    private Long orderId;

    /**
     * 委托类型
     */
    private Integer type;

    /**
     * 委托价格
     */
    private BigDecimal price;

    /**
     * 已成交均价
     */
    private BigDecimal dealAvgPrice;

    /**
     * 委托数量
     */
    private BigDecimal volume;

    /**
     * 已成交量
     */
    private BigDecimal dealVolume;

    /**
     * 预计成交额
     */
    private BigDecimal amount;

    /**
     * 成交总额
     */
    private BigDecimal dealAmount;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 委托时间
     */
    private Date created;
}
