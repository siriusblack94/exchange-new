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
public class AICoinTickerDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    //交易对
    private String symbol;
    //买一价
    private String buy;
    //最近 24 小时最高价
    private String high;
    //最新成交价
    private String last;
    //最近 24 小时最低价
    private String low;
    //卖一价
    private String sell;
    //最近 24 小时成交量
    private String vol;



}
