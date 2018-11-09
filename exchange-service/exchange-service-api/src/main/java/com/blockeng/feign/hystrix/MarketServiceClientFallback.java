package com.blockeng.feign.hystrix;

import com.blockeng.dto.MarketDTO;
import com.blockeng.dto.TradeMarketDTO;
import com.blockeng.feign.MarketServiceClient;
import com.blockeng.framework.enums.ResultCode;
import com.blockeng.framework.http.Response;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author qiang
 */
@Component
public class MarketServiceClientFallback implements MarketServiceClient {

    /**
     * 币币交易市场行情
     *
     * @return
     */
    @Override
    public List<MarketDTO> tradeMarkets() {
        return Collections.emptyList();
    }

    @Override
    public Response markets() {
        return Response.err(ResultCode.ERROR);
    }

    /**
     * 币币交易市场深度
     *
     * @param symbol    交易对标识符
     * @param mergeType 合并深度类型
     * @return
     */
    @Override
    public Response depth(String symbol, String mergeType) {
        return  Response.err(ResultCode.ERROR);
    }

    /**
     * 获取最新成交列表
     *
     * @param symbol 交易对标识符
     * @return
     */
    @Override
    public Response trades(String symbol) {
        return  Response.err(ResultCode.ERROR);
    }

    @Override
    public List<TradeMarketDTO> queryTradeMarkets() {
        return Collections.emptyList();
    }

    @Override
    public List<TradeMarketDTO> queryTradeMarketsByIds(String marketIds) {
        return Collections.emptyList();
    }

    /**
     * 刷新币币交易市场
     *
     * @param marketId 交易对ID
     * @return
     */
    @Override
    public boolean refreshTradeMarket(Long marketId) {
        return false;
    }

    @Override
    public void refresh24hour(String symbol) {

    }

    @Override
    public long getBySymbol(String symbol) {
        return 0L;
    }

    @Override
    public String getMergeType(String symbol) {
        return "0";
    }
}
