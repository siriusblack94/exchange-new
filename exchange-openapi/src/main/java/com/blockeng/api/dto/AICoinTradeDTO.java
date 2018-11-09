package com.blockeng.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * <p>
 * 成交记录
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Data
@Accessors(chain = true)
public class AICoinTradeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    //交易时间
    private String timestamp;
    //交易价格
    private BigDecimal price;
    //交易数量
    private BigDecimal amount;
    //成交方向
    private String side;


}
