package com.blockeng.handle;

import com.blockeng.service.BonusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 持有平台币分红
 *
 * @author qiang
 */
@Component
@Slf4j
public class BonusHandle {

    @Autowired
    private BonusService bonusService;

    @RabbitListener(queues = {"bonus"})
    public void process(String date) {
        try {
            bonusService.tradingDigSet(date);
            bonusService.bonusSet(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
