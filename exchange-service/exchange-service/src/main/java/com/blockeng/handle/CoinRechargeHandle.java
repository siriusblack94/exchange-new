package com.blockeng.handle;

import com.blockeng.entity.Account;
import com.blockeng.entity.Coin;
import com.blockeng.entity.CoinRecharge;
import com.blockeng.framework.enums.BusinessType;
import com.blockeng.framework.utils.GsonUtil;
import com.blockeng.service.AccountService;
import com.blockeng.service.CoinRechargeService;
import com.blockeng.service.CoinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description: 数字货币充值到账
 * @Author: Chen Long
 * @Date: Created in 2018/6/30 下午10:54
 * @Modified by: Chen Long
 */
@Component
@Slf4j
public class CoinRechargeHandle {

    @Autowired
    private CoinService coinService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CoinRechargeService coinRechargeService;

    /**
     * 币种充值
     */
    @RabbitListener(queues = {"finance.recharge.success"})
    public void coinRechargeSuccess(String message) {
        log.info("获取到充值数据:" + message);
        CoinRecharge coinRecharge = GsonUtil.convertObj(message, CoinRecharge.class);
        if (coinRecharge == null || coinRecharge.getUserId() == null || coinRecharge.getAmount() == null) {
            return;
        }
        Coin coin = coinService.queryByName(coinRecharge.getCoinName());
        if (coin == null) {
            return;
        }

        Account account = accountService.queryByUserIdAndCoinId(coinRecharge.getUserId(), coin.getId(), true);
        if (account == null) {
            return;
        }
        try {
            coinRechargeService.insert(coinRecharge.setStatus(3));//3:到账成功
            // 更新资金账户
            accountService.addAmount(coinRecharge.getUserId(),
                    coinRecharge.getCoinId(),
                    coinRecharge.getAmount(),
                    BusinessType.RECHARGE,
                    BusinessType.RECHARGE.getDesc(),
                    coinRecharge.getId());
            log.info("充值完成");
        } catch (Exception e) {
            log.error("插入充值记录异常：" + e.getMessage() + "  充值记录是：" + message);
        }
    }
}
