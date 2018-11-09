package com.blockeng.handle;

import com.blockeng.dto.MatchDTO;
import com.blockeng.framework.constants.Constant;
import com.blockeng.framework.utils.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * 币币交易撮合完成后，更新行情
 *
 * @author qiang
 */
@Component
public class TickerHandler {

    @Autowired
    private CacheMarketHandle cacheMarketHandle;

    @KafkaListener(topics = {Constant.CHANNEL_TICKER_UPDATE})
    public void doHandler(String payload) {
        try {
            MatchDTO matchDTO = GsonUtil.convertObj(payload, MatchDTO.class);
            // 更新缓存
            cacheMarketHandle.refreshMarket(matchDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}