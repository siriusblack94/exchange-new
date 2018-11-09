package com.blockeng.framework.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/5/20 下午4:59
 * @Modified by: Chen Long
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class MatchDTO {

    /**
     * 交易对
     */
    private String symbol;

    /**
     * 买方用户
     */
    private Long buyUserId;

    /**
     * 卖方用户
     */
    private Long sellUserId;

    /**
     * 成交价
     */
    private BigDecimal price;
}
