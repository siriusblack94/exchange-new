package com.blockeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlHelper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.dto.TurnoverData24HDTO;
import com.blockeng.entity.Market;
import com.blockeng.entity.TurnoverOrder;
import com.blockeng.mapper.TurnoverOrderMapper;
import com.blockeng.service.MarketService;
import com.blockeng.service.TurnoverOrderService;
import com.mongodb.Block;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Decimal128;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 成交订单 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
@Slf4j
public class TurnoverOrderServiceImpl extends ServiceImpl<TurnoverOrderMapper, TurnoverOrder> implements TurnoverOrderService {

    @Autowired
    private MarketService marketService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TurnoverOrderMapper turnoverOrderMapper;

    /**
     * 获取最新成交价
     *
     * @param marketId 交易对ID
     * @return
     */
    @Override
    public BigDecimal queryCurrentPrice(Long marketId) {
        BigDecimal price = baseMapper.queryCurrentPrice(marketId);
        if (price == null) {
            // 如果没有成交价则获取交易对配置的开盘价
            Market market = marketService.getMarketById(marketId);
            return market.getOpenPrice();
        }
        return price;
    }

    /**
     * 获取24小时成交数据
     *
     * @param symbol 交易对
     * @return
     */
    @Override
    public TurnoverData24HDTO query24HDealData(String symbol) {
        TurnoverData24HDTO turnoverData24HDTO = new TurnoverData24HDTO();
        Block<Document> processBlock = document -> {
            BigDecimal amount = document.get("amount", Decimal128.class).bigDecimalValue();
            BigDecimal volume = document.get("volume", Decimal128.class).bigDecimalValue();
            turnoverData24HDTO.setAmount(amount);
            turnoverData24HDTO.setVolume(volume);
        };
        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$project", new Document()
                                .append("symbol", 1)
                                .append("amount", 1)
                                .append("volume", 1)
                                .append("created", 1)
                        ),
                new Document()
                        .append("$match", new Document()
                                .append("symbol", symbol)
                                .append("created", new Document()
                                        .append("$gte", new DateTime().minusHours(24).toDate())
                                        .append("$lte", new DateTime().toDate())
                                )
                        ),
                new Document()
                        .append("$group", new Document()
                                .append("_id", "$symbol")
                                .append("amount", new Document()
                                        .append("$sum", "$amount")
                                )
                                .append("volume", new Document()
                                        .append("$sum", "$volume")
                                )
                        )
        );
        mongoTemplate.getCollection("turnover_order").aggregate(pipeline)
                .allowDiskUse(false)
                .forEach(processBlock);
        return turnoverData24HDTO;
    }

    @Override
    public IPage<TurnoverOrder> selectOrders(IPage<TurnoverOrder> page, Wrapper<TurnoverOrder> wrapper) {
        wrapper = (Wrapper<TurnoverOrder>) SqlHelper.fillWrapper(page, wrapper);
        page.setRecords(this.turnoverOrderMapper.selectListPage(page, wrapper));
        return page;
    }
}
