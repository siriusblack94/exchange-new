package com.blockeng.wallet.bitcoin.job;

import com.blockeng.wallet.bitcoin.service.CoinBtcAddressPoolService;
import com.blockeng.wallet.bitcoin.service.CoinBtcRechargeService;
import com.blockeng.wallet.bitcoin.service.CoinBtcWithdrawService;
import com.blockeng.wallet.entity.CoinConfig;
import com.blockeng.wallet.help.ClientInfo;
import com.clg.wallet.enums.CoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class BtcTask {

    @Autowired
    private ClientInfo clientInfo;

    @Autowired
    private CoinBtcRechargeService coinBtcRechargeService;

    @Autowired
    private CoinBtcAddressPoolService coinBtcAddressPoolService;

    @Autowired
    private CoinBtcWithdrawService coinBtcWithdrawService;

    /**
     * 增加btc充值业务
     *
     * @param taskRegistrar
     */
    public void addRecharge(ScheduledTaskRegistrar taskRegistrar) {
        List<CoinConfig> btcTokenCoin = clientInfo.getCoinConfigFormType(CoinType.BTC);//btc token coin
        if (!CollectionUtils.isEmpty(btcTokenCoin)) {
            for (CoinConfig coin : btcTokenCoin) {
                taskRegistrar.addTriggerTask(
                        () -> coinBtcRechargeService.searchRecharge(coin), //1.添加任务内容(Runnable)
                        triggerContext -> new CronTrigger(coin.getTask()).nextExecutionTime(triggerContext)
                );
            }
        }
    }

    /**
     * 创建地址
     *
     * @param taskRegistrar
     */
    public void createAddress(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(
                () -> coinBtcAddressPoolService.createAddress(), //1.添加任务内容(Runnable)
                triggerContext -> {
                    String cron = "0/59 * * * * ?";
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );
    }

    /**
     * 提币
     *
     * @param taskRegistrar
     */
    public void withDraw(ScheduledTaskRegistrar taskRegistrar) {

        taskRegistrar.addTriggerTask(
                () -> coinBtcWithdrawService.transaction(), //1.添加任务内容(Runnable)
                triggerContext -> {
                    String cron = "0/30 * * * * ?";
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );
    }
}
