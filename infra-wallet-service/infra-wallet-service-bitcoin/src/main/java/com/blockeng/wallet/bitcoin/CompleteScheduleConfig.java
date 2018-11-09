package com.blockeng.wallet.bitcoin;

import com.blockeng.wallet.bitcoin.job.BtcTask;
import com.blockeng.wallet.config.CheckCoinStatus;
import com.blockeng.wallet.config.Constant;
import com.blockeng.wallet.jobs.Task;
import com.clg.wallet.enums.CoinType;
import com.blockeng.wallet.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
public class CompleteScheduleConfig implements SchedulingConfigurer {

    private static final Logger LOG = LoggerFactory.getLogger(CompleteScheduleConfig.class);

    @Autowired
    public CommitStatusService commitStatusService;//确认到账服务

    @Autowired
    private CheckCoinStatus checkCoinStatus;

    @Autowired
    private BtcTask btcTask;

    @Autowired
    private Task task;

    /**
     * 执行定时任务.
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        if (checkCoinStatus.allIsRunning()) {
            LOG.info("btc开始运行");
            addBtcTask(taskRegistrar);
        }
    }

    /**
     * btc服务
     *
     * @param taskRegistrar
     */
    private void addBtcTask(ScheduledTaskRegistrar taskRegistrar) {
        if (checkCoinStatus.featuresIsOpen(Constant.FEATURES_RECHARGE)) {
            btcTask.addRecharge(taskRegistrar);
        }
        // btcTask.commitData(taskRegistrar);
        if (checkCoinStatus.featuresIsOpen(Constant.FEATURES_ADDRESS)) {
            btcTask.createAddress(taskRegistrar);
        }
        if (checkCoinStatus.featuresIsOpen(Constant.FEATURES_DRAW)) {
            btcTask.withDraw(taskRegistrar);
        }
        if (checkCoinStatus.featuresIsOpen(Constant.FEATURES_OTHER)) {
            task.service(taskRegistrar, CoinType.BTC);
        }
    }
}
