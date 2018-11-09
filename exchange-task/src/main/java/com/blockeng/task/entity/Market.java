package com.blockeng.task.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 交易对配置信息
 * </p>
 *
 * @author qiang
 * @since 2018-04-17
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Market implements Serializable {

    /**
     * 市场ID
     */
    private Integer id;
    /**
     * 类型：1-数字货币；2：创新交易
     */
    private Integer type;
    /**
     * 交易区域ID
     */
    private Integer tradeAreaId;
    /**
     * 买方币种ID
     */
    private Integer buyCoinid;
    /**
     * 卖方市场ID
     */
    private Integer sellCoinid;
    /**
     * 名称
     */
    private String name;
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
     * 保证金占用比例
     */
    private BigDecimal marginRate;
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
     * 合约单位
     */
    private Integer contractUnit;
    /**
     * 点
     */
    private BigDecimal pointValue;
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
     * 状态
     * 0禁用
     * 1启用
     */
    private Integer status;
    /**
     * 福汇API交易对
     */
    private String fxcmSymbol;
    /**
     * 对应雅虎金融API交易对
     */
    private String yahooSymbol;
    /**
     * 对应阿里云API交易对
     */
    private String aliyunSymbol;
}