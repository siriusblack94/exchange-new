package com.blockeng.api.web.allticker;


import com.alibaba.fastjson.JSONObject;
import com.blockeng.api.dto.AlltickerDTO;
import com.blockeng.api.service.AICoinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 非小号接口
 */

@RestController
@RequestMapping("/openapi")
@Slf4j
@Api(value = "/openapi", description = "非小号行情", tags = "非小号行情")
public class KLineController {
    @Autowired
    AICoinService aiCoinService;
    /**
     * K线行情对接接口
     *
     * @return
     */
    @RequestMapping("/allticker")
    @ApiOperation(value = "K线行情", notes = "K线行情", httpMethod = "GET")
    @ApiImplicitParam(required = true, paramType = "path")
    public String openApiRealTimeQuotes() {
        Date date = new Date();
        String timestamp = ""+date.getTime()/1000;
        JSONObject object = new JSONObject();
        List<AlltickerDTO> list  = (List<AlltickerDTO>) aiCoinService.tickers().get("alltickerDTOS");
        object.put("ticker", list);
        object.put("date", timestamp);
        return object.toJSONString();



    }


//    /**
//     * 行情对接接口
//     *
//     * @return
//     */
//    @RequestMapping("/coinmarketcap/tickers")
//    @ApiOperation(value = "行情", notes = "行情", httpMethod = "GET",
//            authorizations = {@Authorization(value = "Authorization")})
//    @ApiImplicitParam(required = true, paramType = "path")
//    public String coinMarketCap() {
//        JSONObject coinMarketCap = new JSONObject();
//        coinMarketCap = kLineService.coinMarketCap();
//        return coinMarketCap.toJSONString();
//    }


}
