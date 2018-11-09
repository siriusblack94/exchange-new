package com.blockeng.sharding;

import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.util.Map;

@Component
/**
 * 工作室
 */
public class ShardingAutoTableWorkerRoom {

    private Map<String, ExtConfig> extConfig;//基础配置文件
    private ScheduledTaskRegistrar registrar = new ScheduledTaskRegistrar();//定时器注册器

    public void openTheDoor(Map<String, ExtConfig> extConfig, DataSource dataSource, ResourceLoader loader) {
        this.extConfig = extConfig;
        //根据配置启动注册器
        this.extConfig.forEach((key, value) -> {

            if (value.isEnableCreateRoute()) {
                //自动更新路由的定时器任务
                ShardingAutoTableWorker worker = new RouteWorker();
                worker.setDataSource(dataSource);
                worker.setConfig(value);
                worker.setResourceLoader(loader);
                registrar.addCronTask(worker, value.getCreateRouteTime());
                worker.run();
            }
            if (value.isEnableCreateTable()) {
                //自动
                ShardingAutoTableWorker worker = new TableWorker();
                worker.setDataSource(dataSource);
                worker.setConfig(value);
                worker.setResourceLoader(loader);
                registrar.addCronTask(worker, value.getCreateRouteTime());
                worker.run();
            }

        });
    }

    public void startWork() {
        registrar.afterPropertiesSet();
    }

    @PreDestroy
    public void stopWork() {
        registrar.destroy();
    }
}
