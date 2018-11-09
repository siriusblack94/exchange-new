package com.blockeng.admin.web.config;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.entity.Coin;
import com.blockeng.admin.entity.Market;
import com.blockeng.admin.entity.TradeArea;
import com.blockeng.admin.enums.MessageChannel;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.CoinService;
import com.blockeng.admin.service.MarketService;
import com.blockeng.admin.service.TradeAreaService;
import com.blockeng.framework.enums.MarketType;
import com.google.common.base.Strings;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 市场配置信息 前端控制器
 * </p>
 *
 * @author Haliyo
 * @since 2018-05-13
 */
@Slf4j
@RestController
@RequestMapping("/market")
@Api(value = "市场(交易对)配置", description = "市场(交易对)配置管理")
public class MarketController {

    @Autowired
    private MarketService marketService;

    @Autowired
    private TradeAreaService tradeAreaService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Log(value = "查询市场列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('trade_market_query')")
    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "每页显示条数，默认 10", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "交易对(市场)名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "tradeAreaId", value = "交易区域ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "coinId", value = "货币ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态:0 禁用 1 启用", dataType = "String", paramType = "query"),
    })
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "按条件分页查询市场列表", httpMethod = "GET")
    public Object selectPage(@ApiIgnore Page<Market> page,
                             String name, String tradeAreaId,
                             String coinId, String status) {
        EntityWrapper<Market> ew = new EntityWrapper<>();
        ew.eq("type", 1);
        if (!Strings.isNullOrEmpty(name)) {
            ew.like("name", name);
        }
        if (!Strings.isNullOrEmpty(tradeAreaId)) {
            ew.eq("trade_area_id", tradeAreaId);
        }
        if (!Strings.isNullOrEmpty(coinId)) {
            ew.eq("sell_coin_id", coinId);
        }
        if (!Strings.isNullOrEmpty(status)) {
            ew.eq("status", status);
        }
        ew.orderBy("created", false);
        return ResultMap.getSuccessfulResult(marketService.selectPage(page, ew));
    }

    @Log(value = "查询市场信息", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('trade_market_query')")
    @GetMapping("/{id}")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "市场信息", httpMethod = "GET")
    public Object selectOne(@PathVariable String id) {
        return ResultMap.getSuccessfulResult(marketService.selectById(id));
    }

    @Log(value = "查询所有市场列表_币币交易", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('trade_market_query')")
    @GetMapping("/all")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "所有市场列表_币币交易", httpMethod = "GET")
    public Object selectAll() {
        return ResultMap.getSuccessfulResult(marketService.selectList(
                new EntityWrapper<Market>().eq("type", MarketType.TRADE.getCode())));
    }

    @Log(value = "新增市场", type = SysLogTypeEnum.INSERT)
    @PreAuthorize("hasAuthority('trade_market_create')")
    @PostMapping
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "新增市场", httpMethod = "POST")
    public Object insert(@RequestBody Market market) {
        TradeArea tradeArea = tradeAreaService.selectById(market.getTradeAreaId());
        Coin jcCoin = coinService.selectOne(new EntityWrapper<Coin>().eq("id", market.getSellCoinid()));
        Coin bjCoin = coinService.selectOne(new EntityWrapper<Coin>().eq("id", market.getBuyCoinid()));
        if (null == bjCoin || null == jcCoin) {
            return ResultMap.getFailureResult("未找到对应货币!");
        }
        if (jcCoin.getName().equals(bjCoin.getName())) {
            return ResultMap.getFailureResult("报价币和基础币不能相同");
        }
        if (tradeArea != null && !tradeArea.getName().equals(bjCoin.getName())) {
            return ResultMap.getFailureResult("交易区和报价币要一致");
        }
        String name = jcCoin.getName() + "/" + bjCoin.getName();
        Market market1 = marketService.selectOne(new EntityWrapper<Market>().eq("name", name));
        if (market1 != null) {
            return ResultMap.getFailureResult("该市场已经添加，请换一个");
        }
        market.setName(jcCoin.getName() + "/" + bjCoin.getName());
        market.setTitle(jcCoin.getTitle().toUpperCase() + "/" + bjCoin.getTitle().toUpperCase());
        market.setSymbol(jcCoin.getName() + bjCoin.getName());
        market.setImg(jcCoin.getImg());
        market.setMergeDepth(convertMergeDepth(market.getMergeDepth()));
        market = checkMarketParams(market);
        if (marketService.insert(market)) {
            rabbitTemplate.convertAndSend(MessageChannel.REFRESH_MARKET.getChannel(), market.getId());
            return ResultMap.getSuccessfulResult("操作成功!");
        } else {
            return ResultMap.getFailureResult("操作失败!");
        }
    }

    @Log(value = "修改市场", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('trade_market_update')")
    @PostMapping
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "修改市场")
    @RequestMapping(method = RequestMethod.PUT)
    public Object update(@RequestBody Market market) {
        market.setMergeDepth(convertMergeDepth(market.getMergeDepth()));
        market = checkMarketParams(market);
        if (marketService.updateById(market)) {
            rabbitTemplate.convertAndSend(MessageChannel.REFRESH_MARKET.getChannel(), market.getId());
            return ResultMap.getSuccessfulResult("操作成功!");
        } else {
            return ResultMap.getSuccessfulResult("操作失败!");
        }
    }

    @Log(value = "市场状态设置", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('trade_market_update')")
    @PostMapping
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "市场状态设置(status:1启用 0禁用)")
    @RequestMapping(path = "/setStatus", method = RequestMethod.POST)
    public Object setStatus(@RequestBody Market market) {
        Market oriMarket = marketService.selectById(market.getId());
        if (null == oriMarket) {
            return ResultMap.getFailureResult("该记录不存在!");
        }
        if (market.getStatus() > 1
                || market.getStatus() < 0
                || market.getStatus() == null) {
            return ResultMap.getFailureResult("输入参数有误!");
        }
        if (market.getStatus().equals(oriMarket.getStatus())) {
            return ResultMap.getFailureResult("当前状态不能执行此操作!");
        }
        Market newMarket = new Market();
        newMarket.setId(market.getId());
        newMarket.setStatus(market.getStatus());
        if (marketService.updateById(newMarket)) {
            rabbitTemplate.convertAndSend(MessageChannel.REFRESH_MARKET.getChannel(), market.getId());
            return ResultMap.getSuccessfulResult("操作成功!");
        } else {
            return ResultMap.getSuccessfulResult("操作失败!");
        }
    }

    @Log(value = "市场状态设置_批量", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('trade_market_update')")
    @PostMapping
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "市场状态设置_批量")
    @RequestMapping(path = "/batchSetStatus", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "ids:1,2,3", required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "1启用 0禁用", required = true, dataType = "int"),
    })
    public Object batchSetStatus(@RequestBody String ids, Integer status) {
        if (Strings.isNullOrEmpty(ids)) {
            return ResultMap.getFailureResult("ID不能为空!");
        }
        if (status > 1
                || status < 0
                || status == null) {
            return ResultMap.getFailureResult("状态参数有误!");
        }
        List<Market> marketList = marketService.selectList(new EntityWrapper<Market>().in("id", ids));
        List<Market> newMarketCoinList = new ArrayList<>();
        if (marketList.size() < 1) {
            return ResultMap.getFailureResult("记录为空!");
        }
        Market newMarket;
        for (Market market : marketList) {
            if (market.getStatus().equals(status)) {
                return ResultMap.getFailureResult("id:[" + market.getId() + "]当前状态不能执行此操作,请刷新重试!");
            }
            newMarket = new Market();
            newMarket.setId(market.getId());
            newMarket.setStatus(status);
            newMarket.setType(market.getType());
            newMarketCoinList.add(newMarket);
        }
        if (marketService.updateBatchById(newMarketCoinList)) {
            for (Market market : newMarketCoinList) {
                rabbitTemplate.convertAndSend(MessageChannel.REFRESH_MARKET.getChannel(), market.getId());
            }
            return ResultMap.getSuccessfulResult("操作成功!");
        } else {
            return ResultMap.getFailureResult("操作失败!");
        }
    }

    /**
     * 格式化合并深度值
     *
     * @param mergeDepth
     * @return
     */
    private static String convertMergeDepth(String mergeDepth) {
        if (StringUtils.isEmpty(mergeDepth)) {
            return null;
        }
        List<String> mergeDepthList = Arrays.asList(mergeDepth.split(","));
        Collections.sort(mergeDepthList);
        Collections.reverse(mergeDepthList);
        StringBuffer mergeDepthValue = new StringBuffer();
        for (String value : mergeDepthList) {
            mergeDepthValue.append(",").append(value);
        }
        return mergeDepthValue.substring(1);
    }


    /**
     * 检查市场参数配置
     */
    private Market checkMarketParams(Market market) {
        if (null == market.getPriceScale() || market.getPriceScale() > 8) {
            market.setPriceScale(8);
        }

        if (null == market.getNumScale() || market.getNumScale() > 6) {
            market.setNumScale(6);
        }

        market.setFeeBuy(market.getFeeBuy().setScale(4, RoundingMode.DOWN));
        market.setFeeSell(market.getFeeSell().setScale(4, RoundingMode.DOWN));
        market.setOpenPrice(market.getOpenPrice().setScale(8, RoundingMode.DOWN));
        market.setNumMax(market.getNumMax().setScale(8, RoundingMode.DOWN));
        market.setNumMin(market.getNumMin().setScale(8, RoundingMode.DOWN));
        return market;
    }
}
