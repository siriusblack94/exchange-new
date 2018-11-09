package com.blockeng.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/5/14 下午5:40
 * @Modified by: Chen Long
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class DepthItemDTO {

    /**
     * 价格
     */
    private BigDecimal price = BigDecimal.ZERO;
    ;

    /**
     * 数量
     */
    private BigDecimal volume = BigDecimal.ZERO;
}
