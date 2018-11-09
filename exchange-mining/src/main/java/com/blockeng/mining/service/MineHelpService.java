package com.blockeng.mining.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.dto.CoinDTO;
import com.blockeng.dto.TradeMarketDTO;
import com.blockeng.mining.dto.MineTotalDTO;
import com.blockeng.mining.entity.Mine;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 矿池 数据查询
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */

public interface MineHelpService {


    Map<Long, TradeMarketDTO> getCurrentMarket();


    /**
     * 获取挖矿的id
     *
     * @return
     */
    Long getMineCoinId();


    /**
     * 获取挖矿的姓名
     *
     * @return
     */
    String getMineCoinName();

    /**
     * 获取指定Id usdt市场信息
     *
     * @param coinId
     * @return
     */
    TradeMarketDTO getCurrentMarket(Long coinId);

    /**
     * 获得挖矿币种信息
     *
     * @return
     */
    TradeMarketDTO getMineCurrentMarket();

    /**
     * 获取usdt对标cny的价格
     *
     * @return
     */
    String getUsdtToCny();


    /**
     * 获取平台所有币种,按照名称
     */
    Map<String, CoinDTO> getAllCoin();

    /**
     * 按照币种名称,获取币种当前价格
     *
     * @param name
     * @return
     */
    TradeMarketDTO getCurrentMarket(String name);


    /**
     * 获取指定名称和量对应Usdt的价格,如果没有查询到usdt的价格,返回传入数量
     *
     * @param name
     * @return
     */
    BigDecimal getUsdtAmount(String name, BigDecimal amount);

    BigDecimal getCnyAmount(String name, BigDecimal amount);

}
