package com.blockeng.handle;

import com.blockeng.framework.dto.UnlockDTO;
import com.blockeng.framework.utils.GsonUtil;
import com.blockeng.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class PoolUnlockHandle {

    @Autowired
    private AccountService accountService;

    /**
     * 刷新交易对缓存
     */
    @RabbitListener(queues = {"pool.unlock"})
    public void mineUnlock(String msg) {
        UnlockDTO unlockDTO = GsonUtil.convertObj(msg, UnlockDTO.class);
        log.error("为用户增加分红--userid---" + unlockDTO.getUserId() + "---coinid--" + unlockDTO.getCoinId() + "--amount---" + unlockDTO.getAmount() + "---desc----" + unlockDTO.getDesc() + "--orderid--" + unlockDTO.getOrderId());
//        if("每周邀请奖励".equals(unlockDTO.getDesc())) return;
        if(unlockDTO.getAmount().compareTo(BigDecimal.ZERO)<=0) return;
        accountService.addAmount(unlockDTO.getUserId(),
                unlockDTO.getCoinId(),
                unlockDTO.getAmount(),
                unlockDTO.getBusinessType(),
                unlockDTO.getDesc(),
                unlockDTO.getOrderId());

    }
}
