package com.blockeng.wallet.ethereum;

import com.blockeng.wallet.config.CheckCoinStatus;
import com.blockeng.wallet.config.Constant;
import com.blockeng.wallet.ethereum.job.EthTask;
import com.blockeng.wallet.jobs.Task;
import com.clg.wallet.bean.ClientBean;
import com.clg.wallet.enums.CoinType;
import com.clg.wallet.newclient.ClientFactory;
import com.clg.wallet.newclient.EthNewClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.io.*;
import java.math.BigDecimal;

@Configuration
@EnableScheduling
public class CompleteScheduleConfig implements SchedulingConfigurer {

    private static final Logger LOG = LoggerFactory.getLogger(CompleteScheduleConfig.class);

    @Autowired
    private EthTask ethTask;

    @Autowired
    private Task task;


    @Autowired
    private CheckCoinStatus checkCoinStatus;

    /**
     * 执行定时任务.
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        if (checkCoinStatus.allIsRunning()) {
            LOG.info("eth开始运行");
            addEthTask(taskRegistrar);
        }
    }

    /**
     * eth的任务
     *
     * @param taskRegistrar
     */
    private void addEthTask(ScheduledTaskRegistrar taskRegistrar) {
        if (checkCoinStatus.featuresIsOpen(Constant.FEATURES_RECHARGE)) {
            ethTask.addRecharge(taskRegistrar);
        }
        ethTask.commitData(taskRegistrar); //check手续费
        if (checkCoinStatus.featuresIsOpen(Constant.FEATURES_ADDRESS)) {
            ethTask.createAddress(taskRegistrar);
        }
        if (checkCoinStatus.featuresIsOpen(Constant.FEATURES_DRAW)) {
            ethTask.withDraw(taskRegistrar);
        }
        if (checkCoinStatus.featuresIsOpen(Constant.FEATURES_COLLECT)) {
            ethTask.collection(taskRegistrar);
        }
        if (checkCoinStatus.featuresIsOpen(Constant.FEATURES_OTHER)) {
            task.service(taskRegistrar, CoinType.ETH);
        }
    }



}
