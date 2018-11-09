package com.blockeng.web;

import com.blockeng.core.handlers.OrderInHandler;
import com.blockeng.data.MarketData;
import com.blockeng.data.MatchData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author qiang
 */
@RestController
@Slf4j
public class TrustOrderHandler {

    @GetMapping
    public Mono<String> index() {
        Map<String, Object> result = new HashMap<>();
        result.put("in", OrderInHandler.size.intValue());
        result.put("wait", MatchData.queue.size());

        Set<String> keys = MatchData.marketMap.keySet();
        for (String key : keys) {
            MarketData marketData = MatchData.marketMap.get(key);
            result.put(key, marketData.buyQueue.size() + "," + marketData.sellQueue.size());
        }
        return Mono.just(result.toString());
    }

    @GetMapping("/queues")
    public Mono<String> queues(@RequestParam(required = true) String symbol) {
        Map<String, Object> result = new HashMap<>();
        MarketData marketData = MatchData.marketMap.get(symbol);
        result.put("buy", marketData.buyQueue.toString());
        result.put("sell", marketData.sellQueue.toString());
        return Mono.just(result.toString());
    }
}
