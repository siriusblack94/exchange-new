package com.blockeng.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @Description: 创建K线请求数据
 * @Author: Chen Long
 * @Date: Created in 2018/7/1 下午2:49
 * @Modified by: Chen Long
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CreateKLineDTO {

    /**
     * 交易对标识符
     */
    private String symbol;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 成交量
     */
    private BigDecimal volume;
}
