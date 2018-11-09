package com.blockeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.dto.DepthItemDTO;
import com.blockeng.dto.DepthsDTO;
import com.blockeng.dto.TradeEntrustOrderDTO;
import com.blockeng.dto.TradeEntrustOrderDTOMapper;
import com.blockeng.entity.EntrustOrder;
import com.blockeng.entity.Market;
import com.blockeng.entity.TurnoverOrder;
import com.blockeng.framework.constants.Constant;
import com.blockeng.framework.enums.OrderStatus;
import com.blockeng.framework.enums.OrderType;
import com.blockeng.mapper.EntrustOrderMapper;
import com.blockeng.service.EntrustOrderService;
import com.blockeng.service.MarketService;
import com.blockeng.service.TurnoverOrderService;
import com.mongodb.Block;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Decimal128;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 委托订单信息 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
@Slf4j
public class EntrustOrderServiceImpl extends ServiceImpl<EntrustOrderMapper, EntrustOrder> implements EntrustOrderService, Constant {

    @Autowired
    private MarketService marketService;

    @Autowired
    private TurnoverOrderService turnoverOrderService;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 查询未完成委托订单
     *
     * @param symbol 交易对标识符
     * @param userId 用户ID
     * @param page   查询条件及分页参数
     * @return
     */
    @Override
    public IPage<TradeEntrustOrderDTO> queryEntrustOrder(String symbol, long userId, IPage<EntrustOrder> page) {
        QueryWrapper<EntrustOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("symbol", symbol)
                .eq("user_id", userId)
                .eq("status", OrderStatus.PENDING.getCode())
                .orderByDesc("id")
                .last("limit 20");
        List<EntrustOrder> entrustOrders = baseMapper.selectList(wrapper);
        page.setRecords(entrustOrders);
        if (CollectionUtils.isEmpty(entrustOrders)) {
            page.setTotal(0L);
        }
        page.setTotal((long) entrustOrders.size());
        return this.getTradeEntrustOrderDTOPage(page, symbol);
    }

    /**
     * 查询历史委托订单
     *
     * @param symbol 交易对标识符
     * @param userId 用户ID
     * @param page   查询条件及分页参数
     * @return
     */
    @Override
    public IPage<TradeEntrustOrderDTO> queryHistoryEntrustOrder(String symbol, long userId, IPage<EntrustOrder> page) {
        QueryWrapper<EntrustOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("symbol", symbol)
                .eq("user_id", userId)
                .and(i -> i.eq("status", OrderStatus.DEAL.getCode()).or().eq("status", OrderStatus.CANCEL.getCode()))
                .orderByDesc("id")
                .last("limit 20");
        List<EntrustOrder> entrustOrders = baseMapper.selectList(wrapper);
        page.setRecords(entrustOrders);
        if (CollectionUtils.isEmpty(entrustOrders)) {
            page.setTotal(0L);
        }
        page.setTotal((long) entrustOrders.size());
        return this.getTradeEntrustOrderDTOPage(page, symbol);
    }

    @Override
    public DepthsDTO queryDepths(String symbol, BigDecimal mod) {
        DepthsDTO depthsDTO = new DepthsDTO();
        depthsDTO.setAsks(queryDepthItems(symbol, true, mod));
        depthsDTO.setBids(queryDepthItems(symbol, false, mod));
        return depthsDTO;
    }

    private List<DepthItemDTO> queryDepthItems(String symbol, boolean isAsk, BigDecimal mod) {
        int type = isAsk ? 2 : 1;
        int isDesc = isAsk ? 1 : -1;
        List<DepthItemDTO> items = new ArrayList<>();
        Block<Document> processBlock = document -> {
            BigDecimal price = document.get("price", Decimal128.class).bigDecimalValue();
            BigDecimal volume = document.get("volume", Decimal128.class).bigDecimalValue();
            items.add(new DepthItemDTO(price, volume));
        };
        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$project", new Document()
                                .append("price", 1)
                                .append("type", 1)
                                .append("amount", 1)
                                .append("volume", new Document()
                                        .append("$subtract", Arrays.asList(
                                                "$volume",
                                                "$deal"
                                                )
                                        )
                                )
                                .append("deal", 1)
                                .append("symbol", 1)
                                .append("status", 1)
                        ),
                new Document()
                        .append("$match", new Document()
                                .append("status", 0)
                                .append("symbol", symbol)
                                .append("type", type)
                        ),
                new Document()
                        .append("$group", new Document()
                                .append("_id", new Document()
                                        .append("$subtract", Arrays.asList(
                                                "$price",
                                                new Document()
                                                        .append("$mod", Arrays.asList(
                                                                "$price",
                                                                mod
                                                                )
                                                        )
                                                )
                                        )
                                )
                                .append("volume", new Document()
                                        .append("$sum", "$volume")
                                )
                        ),
                new Document()
                        .append("$sort", new Document()
                                .append("_id", isDesc)
                        ),
                new Document()
                        .append("$limit", 60),
                new Document()
                        .append("$project", new Document()
                                .append("price", "$_id")
                                .append("volume", "$volume")
                        )
        );
        mongoTemplate.getCollection("entrust_order").aggregate(pipeline)
                .allowDiskUse(false)
                .forEach(processBlock);
        return items;
    }

    /**
     * 将entrustOrder转换为entrustOrderDTO
     *
     * @param entrustOrderPage
     * @param symbol
     * @return
     */
    private IPage<TradeEntrustOrderDTO> getTradeEntrustOrderDTOPage(IPage<EntrustOrder> entrustOrderPage, String symbol) {
        IPage<TradeEntrustOrderDTO> result = new Page<>();
        result.setSize(entrustOrderPage.getSize())
                .setCurrent(entrustOrderPage.getCurrent())
                .setTotal(entrustOrderPage.getTotal());
        if (!CollectionUtils.isEmpty(entrustOrderPage.getRecords())) {
            List<TradeEntrustOrderDTO> entrustOrderDTOList = new ArrayList<>(entrustOrderPage.getRecords().size());
            Market market = marketService.queryBySymbol(symbol);
            for (EntrustOrder entrustOrder : entrustOrderPage.getRecords()) {
                TradeEntrustOrderDTO entrustOrderDTO = TradeEntrustOrderDTOMapper.INSTANCE.from(entrustOrder);
                entrustOrderDTOList.add(entrustOrderDTO);
                entrustOrderDTO.setOrderId(entrustOrder.getId());
                entrustOrderDTO.setDealVolume(entrustOrder.getDeal());
                QueryWrapper<TurnoverOrder> turnoverOrderWrapper = new QueryWrapper<>();
                turnoverOrderWrapper.eq("symbol", symbol)
                        .eq("status", OrderStatus.DEAL.getCode());
                if (entrustOrder.getType() == OrderType.BUY.getCode()) {
                    turnoverOrderWrapper.eq("buy_order_id", entrustOrder.getId());
                } else {
                    turnoverOrderWrapper.eq("sell_order_id", entrustOrder.getId());
                }
                List<TurnoverOrder> turnoverOrderList = turnoverOrderService.selectList(turnoverOrderWrapper);
                BigDecimal dealAmount = BigDecimal.ZERO;
                for (TurnoverOrder turnoverOrder : turnoverOrderList) {
                    dealAmount = dealAmount.add(turnoverOrder.getAmount());
                }
                entrustOrderDTO.setDealAvgPrice(BigDecimal.ZERO);
                if (entrustOrder.getDeal().compareTo(BigDecimal.ZERO) == 1) {
                    BigDecimal dealAvgPrice = dealAmount.divide(entrustOrder.getDeal(), market.getPriceScale(), RoundingMode.HALF_UP);
                    entrustOrderDTO.setDealAvgPrice(dealAvgPrice);
                }
                entrustOrderDTO.setDealAmount(dealAmount);
            }
            result.setRecords(entrustOrderDTOList);
        }
        return result;
    }
}
