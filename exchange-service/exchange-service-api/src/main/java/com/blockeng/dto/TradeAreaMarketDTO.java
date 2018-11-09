package com.blockeng.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/5/14 上午10:15
 * @Modified by: Chen Long
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class TradeAreaMarketDTO {

    /**
     * 交易区域名称
     */
    private String areaName;

    /**
     * 交易对列表
     */
    private List<TradeMarketDTO> markets;
}