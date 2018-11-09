package com.blockeng.wallet.jobs;

import com.blockeng.wallet.service.CoinBalanceService;
import com.blockeng.wallet.service.CoinServerService;
import com.blockeng.wallet.service.FeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Component
public class Task {

    private static final Logger LOG = LoggerFactory.getLogger(Task.class);


    @Autowired
    private CoinServerService coinServerService;


    @Autowired
    private CoinBalanceService coinBalanceService;


    @Autowired
    private FeeService feeService;

    /**
     * 服务
     *
     * @param taskRegistrar
     */
    public void service(ScheduledTaskRegistrar taskRegistrar, String type) {

        taskRegistrar.addTriggerTask(
                () -> otherJob(type), //1.添加任务内容(Runnable)
                triggerContext -> {
                    String cron = "0/59 * * * * ? ";
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );
    }

    /**
     * 其他的一些定时任务丢在这个里面
     *
     * @param type
     */
    private void otherJob(String type) {
        try {
            coinServerService.checkServer(type);
            coinBalanceService.checkBalance(type);
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

    }
}
