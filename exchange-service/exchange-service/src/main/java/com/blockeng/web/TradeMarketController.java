package com.blockeng.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blockeng.dto.*;
import com.blockeng.entity.Market;
import com.blockeng.framework.constants.Constant;
import com.blockeng.framework.enums.DepthMergeType;
import com.blockeng.framework.enums.KlineType;
import com.blockeng.framework.enums.MarketType;
import com.blockeng.framework.exception.GlobalDefaultException;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.security.UserDetails;
import com.blockeng.framework.utils.GsonUtil;
import com.blockeng.service.EntrustOrderService;
import com.blockeng.service.MarketService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @Description: 币币交易市场Controller
 * @Author: Chen Long
 * @Date: Created in 2018/5/14 上午9:57
 * @Modified by: Chen Long
 */
@RestController
@RequestMapping("/trade/market")
@Slf4j
@Api(value = "币币交易市场", description = "币币交易市场 REST API")
public class TradeMarketController implements Constant {

    @Autowired
    private MarketService marketService;

    @Autowired
    private EntrustOrderService entrustOrderService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RedissonClient redisson;

    /**
     * 首页交易市场信息
     *
     * @return
     */
    @ApiIgnore
    @GetMapping("/area")
    @ApiOperation(value = "首页市场信息", notes = "首页市场信息", httpMethod = "GET")
    @ResponseBody
    public Object markets() {
        List<TradeAreaMarketDTO> tradeAreaMarket = marketService.queryTradeMarkets();
        return Response.ok(tradeAreaMarket);
    }

    /**
     * 首页交易市场信息
     *
     * @return
     */
    @GetMapping("/{areaId}")
    @ApiOperation(value = "首页市场信息", notes = "首页市场信息", httpMethod = "GET")
    @ResponseBody
    public Object markets(@PathVariable("areaId") Long areaId) {
        TradeAreaMarketDTO tradeAreaMarket = marketService.queryTradeMarkets(areaId);
        if (!Optional.ofNullable(tradeAreaMarket).isPresent()) {
            //交易区不存在
            throw new GlobalDefaultException(50037);
        }
        return Response.ok(tradeAreaMarket);
    }

    /**
     * 个人收藏交易市场信息
     *
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/favorite")
    @ApiOperation(value = "个人收藏交易市场信息", notes = "个人收藏交易市场信息", httpMethod = "GET"
            , authorizations = {@Authorization(value = "Authorization")})
    @ResponseBody
    public Object favorite(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        List<TradeAreaMarketDTO> tradeAreaMarket = marketService.queryFavoriteTradeMarkets(userDetails.getId());
        return Response.ok(tradeAreaMarket);
    }

    /**
     * 获取K线数据
     *
     * @param symbol   获取K线数据请求参数
     * @param lintType 获取K线数据请求参数
     * @return
     */
    @GetMapping(value = "/kline/{symbol}/{lintType}")
    @ApiOperation(value = "币币交易K线数据", notes = "lineType取值范围<br/>" +
            "5sec: 5秒钟<br />" +
            "1min: 1分钟<br />" +
            "5min: 5分钟<br />" +
            "15min: 15分钟<br />" +
            "30min: 30分钟<br />" +
            "1hour: 1小时<br />" +
            "2hour: 2小时<br />" +
            "4hour: 4小时<br />" +
            "6hour: 6小时<br />" +
            "12hour: 12小时<br />" +
            "1day: 1天<br />" +
            "1week: 1周<br />" +
            "1mon: 1月<br />" +
            "1year: 1年<br />", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "symbol", value = "交易对标识符", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "lintType", value = "K线类型", required = true, dataType = "String", paramType = "path")
    })
    @ResponseBody
    public Object kline(@PathVariable("symbol") String symbol,
                        @PathVariable("lintType") String lintType) {
        String redisKey = new StringBuffer(REDIS_KEY_TRADE_KLINE).append(symbol).append(":").append(lintType).toString();
        List<Object> kline = redisTemplate.opsForList().range(redisKey, 0, 999);
        Collections.reverse(kline);
        return Response.ok(GsonUtil.toJson(kline));
    }

    /**
     * 获取市场合并深度
     *
     * @param symbol    交易对标识符
     * @param mergeType 合并深度类型
     */
    @GetMapping(value = "/depth/{symbol}/{mergeType}")
    @ApiOperation(value = "币币交易市场深度", notes = "币币交易市场深度", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "symbol", value = "交易对标识符", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "mergeType", value = "合并深度类型", required = true, dataType = "String", paramType = "path")
    })
    public Response depth(@PathVariable("symbol") String symbol,
                          @PathVariable("mergeType") String mergeType) {
        DepthMergeType depthMergeType = DepthMergeType.getByCode(mergeType);
        if (!Optional.ofNullable(depthMergeType).isPresent()) {
            //Depth 类型不支持
            throw new GlobalDefaultException(50038);
        }
        Market market = marketService.queryBySymbol(symbol);
        if (!Optional.ofNullable(market).isPresent()) {
            //交易市场错误
            throw new GlobalDefaultException(50002);
        }
        String[] mergeDepthArray = market.getMergeDepth().split(",");
        int scale = Integer.parseInt(mergeDepthArray[depthMergeType.getValue()]);
        BigDecimal number = new BigDecimal(Math.pow(10, scale));
        BigDecimal mod = BigDecimal.ONE.divide(number, scale, RoundingMode.HALF_UP);
        DepthsDTO depths = entrustOrderService.queryDepths(symbol, mod);
        RMap<Long, TradeMarketDTO> marketMap = redisson.getMap("MARKET_CACHE");
        TradeMarketDTO tradeMarketDTO = marketMap.get(market.getId());
        depths.setPrice(tradeMarketDTO.getPrice()).setCnyPrice(tradeMarketDTO.getCnyPrice());
        return Response.ok(depths);
    }

    /**
     * 获取交易对行情数据
     *
     * @param symbol 交易对标识符
     * @return
     */
    @GetMapping(value = "/ticker/{symbol}")
    @ApiOperation(value = "币币交易实时行情", notes = "币币交易实时行情", httpMethod = "GET")
    @ApiImplicitParam(name = "symbol", value = "交易对标识符", required = true, dataType = "String", paramType = "path")
    public Object ticker(@PathVariable("symbol") String symbol) {
        String redisKey = new StringBuffer(REDIS_KEY_TRADE_KLINE).append(symbol).append(":").append(KlineType.ONE_MINUTES.getValue()).toString();
        Object kline = redisTemplate.opsForList().index(redisKey, 0);
        return Response.ok(GsonUtil.toJson(kline));
    }

    /**
     * 获取最新成交列表
     *
     * @param symbol 交易对标识符
     * @return
     */
    @GetMapping(value = "/trades/{symbol}")
    @ApiOperation(value = "币币交易最新成交列表", notes = "币币交易最新成交列表", httpMethod = "GET")
    @ApiImplicitParam(name = "symbol", value = "交易对标识符", required = true, dataType = "String", paramType = "path")
    @ResponseBody
    public Response trades(@PathVariable("symbol") String symbol) {
        // 从MongoDB中查询最新成交记录
        Query query = new Query(Criteria.where("symbol").is(symbol));
        query.with(new Sort(Sort.Direction.DESC, "created")).limit(60);
        List<TradeDealOrderDTO> txList = mongoTemplate.find(query, TradeDealOrderDTO.class);
        return Response.ok(txList);
    }

    /**
     * 获取所有币币交易市场
     *
     * @return
     */
    @GetMapping
    public Object tradeMarketList() {
        return Response.ok(marketService.queryMarkets());
    }

    /****************************************** 微服务 *******************************************************/
    /**
     * 获取所有币币交易市场
     *
     * @return
     */
    @ApiIgnore
    @GetMapping("/all")
    public List<MarketDTO> tradeMarkets() {
        return marketService.queryMarkets();
    }

    /**
     * 刷新24小时成交数据
     *
     * @return
     */
    @ApiIgnore
    @GetMapping("/refresh_24hour")
    public void refresh24HDeal(String symbol) {
        marketService.refresh24HDeal(symbol);
    }

    /**
     * 根据marketIds获取币币交易市场
     *
     * @return
     */
    @ApiIgnore
    @PostMapping("/queryTradeMarkets")
    public List<TradeMarketDTO> queryTradeMarkets() {
        Collection<TradeMarketDTO> data = marketService.queryTradeMarketList();
        List<TradeMarketDTO> tradeMarkets = new ArrayList<>(data);
        Collections.sort(tradeMarkets);
        return tradeMarkets;
    }

    /**
     * 根据marketIds获取币币交易市场
     *
     * @return
     */
    @ApiIgnore
    @PostMapping("/queryTradeMarketsByIds")
    public List<TradeMarketDTO> queryTradeMarketsByIds(String marketIds) {
        Collection<TradeMarketDTO> data = marketService.queryByType(MarketType.TRADE, marketIds);
        List<TradeMarketDTO> tradeMarkets = new ArrayList<>(data);
        Collections.sort(tradeMarkets);
        return tradeMarkets;
    }

    /**
     * 根据Symbol 获取 marketId
     */
    @GetMapping("/getBySymbol/{symbol}")
    public Object getBySymbol(@PathVariable("symbol") String symbol) {
        QueryWrapper<Market> e = new QueryWrapper<>();
        e.eq("symbol", symbol);
        Market market = marketService.selectOne(e);
        if (Optional.ofNullable(market).isPresent()) {
            return market.getId();
        }
        return 0L;
    }
    /**
     *合并深度类型
     */
    @GetMapping(value = "/getMergeType/{symbol}")
    public Object getMergeType(@PathVariable("symbol") String symbol) {
        DepthMergeType depthMergeType = DepthMergeType.getByCode("step0");
        Market market = marketService.queryBySymbol(symbol);
        if (market==null|| market.getMergeDepth()==null) return "0";
        String[] mergeDepthArray = market.getMergeDepth().split(",");
        String s = mergeDepthArray[depthMergeType.getValue()];
        if (StringUtils.isBlank(s)) {
            return "0";
        }
        return s;
    }
}
