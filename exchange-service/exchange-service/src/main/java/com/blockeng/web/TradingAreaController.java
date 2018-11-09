package com.blockeng.web;

import com.blockeng.dto.TradeAreaDTO;
import com.blockeng.framework.enums.TradeAreaType;
import com.blockeng.framework.http.Response;
import com.blockeng.service.TradeAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author qiang
 */
@RestController
@RequestMapping("/trading_area")
public class TradingAreaController {

    @Autowired
    private TradeAreaService tradeAreaService;

    @GetMapping
    Object tradingArea() {
        List<TradeAreaDTO> tradeAreaList = tradeAreaService.queryByType(TradeAreaType.DC_TYPE);
        return Response.ok(tradeAreaList);
    }

    @GetMapping("/list")
    Object list() {
        List<TradeAreaDTO> tradeAreaList = tradeAreaService.queryByType(TradeAreaType.DC_TYPE);
        return tradeAreaList;
    }
}
