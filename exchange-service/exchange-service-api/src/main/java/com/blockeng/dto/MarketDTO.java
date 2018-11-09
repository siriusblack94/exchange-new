package com.blockeng.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @Description: 交易市场数据传输对象
 * @Author: Chen Long
 * @Date: Created in 2018/5/14 下午3:07
 * @Modified by: Chen Long
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class MarketDTO {

    /**
     * 市场ID
     */
    private Long id;

    /**
     * 交易区域ID
     */
    private Long tradeAreaId;

    /**
     * 卖方市场ID
     */
    private Long sellCoinId;

    /**
     * 买方币种ID
     */
    private Long buyCoinId;

    /**
     * 名称
     */
    private String name;

    /**
     * 交易对标识
     */
    private String symbol;

    /**
     * 标题
     */
    private String title;

    /**
     * 市场logo
     */
    private String img;

    /**
     * 开盘价格
     */
    private BigDecimal openPrice;

    /**
     * 买入手续费率
     */
    private BigDecimal feeBuy;

    /**
     * 卖出手续费率
     */
    private BigDecimal feeSell;

    /**
     * 单笔最小委托量
     */
    private BigDecimal numMin;

    /**
     * 单笔最大委托量
     */
    private BigDecimal numMax;

    /**
     * 单笔最小成交额
     */
    private BigDecimal tradeMin;

    /**
     * 单笔最大成交额
     */
    private BigDecimal tradeMax;

    /**
     * 价格小数位
     */
    private Integer priceScale;

    /**
     * 数量小数位
     */
    private Integer numScale;

    /**
     * 合并深度(格式：0.0001,0.001,0.01)
     */
    private String mergeDepth;

    /**
     * 交易时间
     */
    private String tradeTime;

    /**
     * 交易周期
     */
    private String tradeWeek;

    /**
     * 排序列
     */
    private Integer sort;

    /**
     * 状态：0-禁用；1-启用
     */
    private Integer status;
}
