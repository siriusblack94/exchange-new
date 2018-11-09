package com.blockeng.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author qiang
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class EntrustOrderMatchDTO implements Serializable {

    private static final long serialVersionUID = 2001733705944713209L;

    /**
     * 订单ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 交易对ID
     */
    private Long marketId;

    /**
     * 交易对标识符
     */
    private String symbol;

    /**
     * 交易对名称
     */
    private String marketName;

    /**
     * 币种ID
     */
    private Long coinId;

    /**
     * 委托价格
     */
    private BigDecimal price;

    /**
     * 手续费费率
     */
    private BigDecimal feeRate;

    /**
     * 委托数量
     */
    private BigDecimal volume;

    /**
     * 已成交
     */
    private BigDecimal deal;

    /**
     * 买卖类型：1-买入；2-卖出
     */
    private Integer type;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 状态
     */
    private Integer status;
}
