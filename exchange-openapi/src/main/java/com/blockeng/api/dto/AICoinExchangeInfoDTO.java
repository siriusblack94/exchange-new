package com.blockeng.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * <p>
 * 成交订单
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Data
@Accessors(chain = true)
public class AICoinExchangeInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    //交易对
    private String symbol;
    //交易对状态
    private String status;
    //基础货币
    private String baseAsset;
    //基础货币精度
    private Integer baseAssetPrecision;
    //计价货币
    private String quoteAsset;
    //计价货币精度
    private Integer quoteAssetPrecision;




}
