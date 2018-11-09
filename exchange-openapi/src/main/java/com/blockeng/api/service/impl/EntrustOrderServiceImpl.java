package com.blockeng.api.service.impl;

import com.blockeng.api.dto.DepthsVO;
import com.blockeng.api.service.EntrustOrderService;
import com.blockeng.framework.constants.Constant;
import com.mongodb.Block;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Decimal128;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 查询
 * </p>
 *
 * @author haoxiaoping
 * @since 2018-08-15
 */
@Slf4j
@Service
public class EntrustOrderServiceImpl implements EntrustOrderService, Constant {


    @Autowired
    private MongoTemplate mongoTemplate;




    @Override
    public DepthsVO queryDepths(String symbol, BigDecimal mod, int size) {
        DepthsVO depthsVO = new DepthsVO();
        depthsVO.setAsks(queryDepthItems(symbol, true, mod, size));
        depthsVO.setBids(queryDepthItems(symbol, false, mod, size));
        return depthsVO;
    }

    private List<ArrayList> queryDepthItems(String symbol, boolean isAsk, BigDecimal mod, int size) {
        int type = isAsk ? 2 : 1;
        int isDesc = isAsk ? 1 : -1;
        List<ArrayList> items = new ArrayList<>();
        Block<Document> processBlock = document -> {
            BigDecimal price = document.get("price", Decimal128.class).bigDecimalValue();
            BigDecimal volume = document.get("volume", Decimal128.class).bigDecimalValue();
            ArrayList<BigDecimal> objects = new ArrayList<>();
            objects.add(price);
            objects.add(volume);
            items.add(objects);
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
                        .append("$limit", size),
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



}
