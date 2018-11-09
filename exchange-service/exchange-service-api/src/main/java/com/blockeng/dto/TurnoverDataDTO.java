package com.blockeng.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @Description: 成交数据
 * @Author: Chen Long
 * @Date: Created in 2018/5/15 下午6:11
 * @Modified by: Chen Long
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class TurnoverDataDTO {

    /**
     * 平均成交价
     */
    private BigDecimal price = BigDecimal.ZERO;

    /**
     * 成交数量
     */
    private BigDecimal volume = BigDecimal.ZERO;
}
