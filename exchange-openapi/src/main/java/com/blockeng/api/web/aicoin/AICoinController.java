package com.blockeng.api.web.aicoin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blockeng.api.service.AICoinService;
import com.blockeng.framework.utils.GsonUtil;
import com.blockeng.api.dto.AICoinExchangeInfoDTO;
import com.blockeng.api.dto.AICoinTickerDTO;
import com.blockeng.api.dto.AICoinTradeDTO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * AICoin对接接口
 */

@RestController
@RequestMapping("/aicoin")
@Slf4j
@Api(value = "/aicoin", description = "AICoin 接入行情所需接口", tags = "AICoin 接入行情所需接口")
public class AICoinController {

    @Autowired
    private AICoinService aICoinService;

    /**
     * 行情接口
     *
     * @return
     */
    @RequestMapping("/tickers")
    @ApiOperation(value = "行情", notes = "行情", httpMethod = "GET")
    @ApiImplicitParam(required = true, paramType = "path")
    public String tickers() {
        JSONObject object = new JSONObject();
        List<AICoinTickerDTO> list = (List<AICoinTickerDTO>) aICoinService.tickers().get("aiCoinTickerDTOS");
        String createdDate = String.valueOf(System.currentTimeMillis());
        object.put("ticker", list);
        object.put("timestamp", createdDate);
        return object.toJSONString();
    }

    /**
     * 深度接口
     * @return
     */
    @RequestMapping("/depth")
    @ApiOperation(value = "深度接口", notes = "深度接口", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "symbol", value = "交易对标识符", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "20条数据", required = true, dataType = "int", paramType = "path")
    })
    public String depth(@RequestParam String symbol, @RequestParam int size) {
//        log.info("==AICoinController==depth==symbol=" + symbol + ",size=" + size);
        return JSON.toJSONString(aICoinService.depth(symbol, size));
    }

    /**
     * 最新成交记录接口
     *
     * @return
     */
    @RequestMapping("/trades")
    @ApiOperation(value = "成交记录", notes = "成交记录", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "symbol", value = "交易对标识符", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "50条数据", required = true, dataType = "int", paramType = "path")
    })
    public String trades(@RequestParam String symbol, @RequestParam int size) {
        List<AICoinTradeDTO> list =aICoinService.trades(symbol, size);
//        log.info("==AICoinController==tickers==list.size=" + list.size());
        return GsonUtil.toJson(list);
    }

    /**
     * 1 min k线接口(周期为一分钟，以时间降序返回最新的 K 线数据)
     *
     * @return
     */
    @RequestMapping("/kline")
    @ApiOperation(value = "k线接口1min", notes = "k线接口1min", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "symbol", value = "交易对标识符", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "type", value = "k线周期为1分钟  1min", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "100条最新记录", required = true, dataType = "int", paramType = "path")
    })
    public String klineOneMin(@RequestParam String symbol, @RequestParam String type, @RequestParam int size) {
        if (size<0||size>1000) size=1;
        List<Object> list = aICoinService.klineOneMin(symbol, type, size);
//        log.info("==AICoinController==kline==list.size=" + list.size());
        return GsonUtil.toJson(list);
    }

    /**
     * 交易对信息接口（用于 AICoin 自动上币）
     *
     * @return
     */
    @RequestMapping("/exchangeInfo")
    @ApiOperation(value = "交易对信息", notes = "交易对信息", httpMethod = "GET")
    @ApiImplicitParam(required = true, paramType = "path")
    public String exchangeInfo() {
        List<AICoinExchangeInfoDTO> list = aICoinService.exchangeInfo();
//        log.info("==AICoinController==exchangeInfo==list.size=" + list.size());
        return GsonUtil.toJson(list);
    }


}



