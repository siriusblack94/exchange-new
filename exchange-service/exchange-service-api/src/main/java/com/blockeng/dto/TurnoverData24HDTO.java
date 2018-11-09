package com.blockeng.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/3/26 下午6:12
 * @Modified by: Chen Long
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class TurnoverData24HDTO {

    /**
     * 24小时成交量
     */
    private BigDecimal volume = BigDecimal.ZERO;

    /**
     * 24小时成交总额
     */
    private BigDecimal amount = BigDecimal.ZERO;
}
