package com.blockeng.boot;

import com.blockeng.core.handlers.OrderInHandler;
import com.blockeng.data.MatchData;
import com.blockeng.entity.EntrustOrder;
import com.blockeng.model.Order;
import com.blockeng.repository.OrderRepository;
import com.blockeng.vo.mappers.OrderMapper;
import com.mongodb.client.MongoCollection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qiang
 */
@Slf4j
public class LoadOrderDataRunner {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private OrderRepository orderRepository;

    public static ConcurrentHashMap<Long, EntrustOrder> mysqlLoadData = new ConcurrentHashMap();


    @PostConstruct
    public void run() {
        StopWatch stopWatch = new StopWatch(); //计时器，记录数据加载时间?
        stopWatch.start("开始加载未撮合订单...");
        MongoCollection mongoCollection = mongoTemplate.getCollection("entrust_order");
        mongoCollection.drop();
        String sql = "SELECT id, user_id, market_id, symbol, market_name, price, ( volume - deal ) AS volume, amount, fee_rate, fee, 0 AS deal, freeze, type, status, last_update_time, created FROM entrust_order WHERE `status` = 0 ORDER BY created ASC";
        mongoTemplate.indexOps(EntrustOrder.class).ensureIndex(new Index().on("symbol", Sort.Direction.ASC));
        mongoTemplate.indexOps(EntrustOrder.class).ensureIndex(new Index().on("price", Sort.Direction.ASC));
        mongoTemplate.indexOps(EntrustOrder.class).ensureIndex(new Index().on("user_id", Sort.Direction.ASC));
        mongoTemplate.indexOps(EntrustOrder.class).ensureIndex(new Index().on("volume", Sort.Direction.ASC));
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
        List<EntrustOrder> pool = new ArrayList<>(200);

        while (sqlRowSet.next()) {
            EntrustOrder entrustOrder = new EntrustOrder();
            long id = sqlRowSet.getLong("id");
            long user_id = sqlRowSet.getLong("user_id");
            long market_id = sqlRowSet.getLong("market_id");
            String symbol = sqlRowSet.getString("symbol");
            String market_name = sqlRowSet.getString("market_name");
            BigDecimal price = sqlRowSet.getBigDecimal("price");
            BigDecimal volume = sqlRowSet.getBigDecimal("volume");
            BigDecimal amount = sqlRowSet.getBigDecimal("amount");
            BigDecimal fee_rate = sqlRowSet.getBigDecimal("fee_rate");
            BigDecimal fee = sqlRowSet.getBigDecimal("fee");
            BigDecimal deal = sqlRowSet.getBigDecimal("deal");
            BigDecimal freeze = sqlRowSet.getBigDecimal("freeze");
            int type = sqlRowSet.getInt("type");
            int status = sqlRowSet.getInt("status");
            Date lastUpdateTime = sqlRowSet.getDate("last_update_time");
            Date created = sqlRowSet.getDate("created");
            entrustOrder.setId(id);
            entrustOrder.setUserId(user_id);
            entrustOrder.setMarketId(market_id);
            entrustOrder.setSymbol(symbol);
            entrustOrder.setMarketName(market_name);
            entrustOrder.setPrice(price);
            entrustOrder.setVolume(volume);
            entrustOrder.setAmount(amount);
            entrustOrder.setFeeRate(fee_rate);
            entrustOrder.setFee(fee);
            entrustOrder.setDeal(deal);
            entrustOrder.setFreeze(freeze);
            entrustOrder.setType(type);
            entrustOrder.setStatus(status);
            entrustOrder.setLastUpdateTime(lastUpdateTime);
            entrustOrder.setCreated(created);
            pool.add(entrustOrder);
            mysqlLoadData.put(entrustOrder.getId(), entrustOrder);
            if (pool.size() >= 200 || sqlRowSet.isLast()) {
                orderRepository.saveAll(pool);
                pool.clear();
                log.info("loading " + OrderInHandler.size.get());
            }
            OrderInHandler.size.getAndIncrement();
            Order order = OrderMapper.INSTANCE.form(entrustOrder);
            MatchData.queue.offer(order);
        }
        stopWatch.stop();
        log.info("=====> Load time：{}", stopWatch.getTotalTimeMillis());
    }
}
