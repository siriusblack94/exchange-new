package com.blockeng.handle;

import com.blockeng.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 为新注册用户生成资金账户
 *
 * @author qiang
 */
@Component
@Slf4j
public class SyncAccountHandler {

    @Autowired
    private AccountService accountService;

    @RabbitListener(queues = {"sync.account"})
    public void handle(long userId) {
        try {
            accountService.syncAccount(userId);
        } catch (Exception e) {
            log.info(e.toString());
        }
    }
}
