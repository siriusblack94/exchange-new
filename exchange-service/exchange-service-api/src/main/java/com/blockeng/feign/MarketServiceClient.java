package com.blockeng.feign;

import com.blockeng.dto.MarketDTO;
import com.blockeng.dto.TradeMarketDTO;
import com.blockeng.feign.hystrix.MarketServiceClientFallback;
import com.blockeng.framework.http.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/5/15 下午4:43
 * @Modified by: Chen Long
 */
@FeignClient(value = "exchange-service", fallback = MarketServiceClientFallback.class)
public interface MarketServiceClient {

    /**
     * 币币交易市场行情
     *
     * @return
     */
    @RequestMapping(value = "/trade/market/all", method = RequestMethod.GET)
    List<MarketDTO> tradeMarkets();



    @RequestMapping(value = "/trade/market/area", method = RequestMethod.GET)
    Response markets();

    /**
     * 币币交易市场深度
     *
     * @param symbol    交易对标识符
     * @param mergeType 合并深度类型
     * @return
     */
    @RequestMapping(value = "/trade/market/depth/{symbol}/{mergeType}", method = RequestMethod.GET)
    Response depth(@PathVariable("symbol") String symbol, @PathVariable("mergeType") String mergeType);

    /**
     * 获取最新成交列表
     *
     * @param symbol 交易对标识符
     * @return
     */
    @RequestMapping(value = "/trade/market/trades/{symbol}", method = RequestMethod.GET)
    Response trades(@PathVariable("symbol") String symbol);

    @RequestMapping(value = "/trade/market/queryTradeMarkets", method = RequestMethod.POST)
    List<TradeMarketDTO> queryTradeMarkets();

    @RequestMapping(value = "/trade/market/queryTradeMarketsByIds", method = RequestMethod.POST)
    List<TradeMarketDTO> queryTradeMarketsByIds(@RequestParam("marketIds") String marketIds);

    /**
     * 刷新币币交易市场
     *
     * @param marketId 交易对ID
     * @return
     */
    @RequestMapping(value = "/trade/market/refresh/{marketId}", method = RequestMethod.GET)
    boolean refreshTradeMarket(@PathVariable("marketId") Long marketId);

    /**
     * 刷新24小时成交数据
     *
     * @param symbol 交易对
     * @return
     */
    @RequestMapping(value = "/trade/market/refresh_24hour", method = RequestMethod.GET)
    void refresh24hour(@RequestParam("symbol") String symbol);

    /**
     * 根据Symbol获取market数据
     */
    @GetMapping(value = "/trade/market/getBySymbol/{symbol}")
    long getBySymbol(@PathVariable("symbol") String symbol);
    /**
     *
     * 深度类型
     * @param symbol
     * @return
     */
    @GetMapping(value = "/trade/market/getMergeType/{symbol}")
    String getMergeType(@PathVariable("symbol") String symbol);

}