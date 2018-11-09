package com.blockeng.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/5/14 下午3:42
 * @Modified by: Chen Long
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class DepthsDTO {

    /**
     * 委托买单
     */
    private List<DepthItemDTO> bids = new ArrayList<>();

    /**
     * 委托卖单
     */
    private List<DepthItemDTO> asks = new ArrayList<>();

    /**
     * 当前成交价
     */
    private BigDecimal price;

    /**
     * 当前成交价对应CNY价格
     */
    private BigDecimal cnyPrice;
}
