package com.blockeng.sharding;

import io.shardingsphere.core.jdbc.core.datasource.MasterSlaveDataSource;
import io.shardingsphere.core.jdbc.core.datasource.ShardingDataSource;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static com.blockeng.sharding.DateUtil.toLocalDate;


/**
 * maple
 */

@Configuration
@ConfigurationProperties(prefix = "sharding.jdbc.config")
@EnableAutoConfiguration
@EqualsAndHashCode(callSuper = false)
@Import(ShardingAutoTableWorkerRoom.class)
@Data
@Slf4j
public class ShardingConfig implements ApplicationListener<ApplicationEvent>, ResourceLoaderAware {

    @Autowired
    BeginDateConfig dateConfig;
    //加载配置
    private Map<String, String> ext;
    //转换后的配置
    private Map<String, ExtConfig> extConfig = new HashMap<>();

    @Autowired
    private ShardingAutoTableWorkerRoom room; //定时工作器的容器

    private ResourceLoader loader; //spring 资源读取器

    private boolean loaded = false; //spring ready 事件状态

    /**
     * Spring 事件监听
     *
     * @param event
     */
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationReadyEvent && !loaded) { //ready事件
            log.info("Start loading ext sharding configs");
            if (ext != null && ext.size() > 0) {
                try {

                    DataSource dataSource = (DataSource) ((ApplicationReadyEvent) event).getApplicationContext().getBean("dataSource");

                    if (dataSource instanceof MasterSlaveDataSource) {
                        dataSource = (DataSource) ((ApplicationReadyEvent) event).getApplicationContext().getBean("shardingSlaveDatasource");
                        log.info("Get MasterSlave datasource!");
                        if (!(dataSource instanceof ShardingDataSource)) {
                            log.info("Skip and no sharding datasource");
                            return;
                        }
                    } else if (dataSource instanceof ShardingDataSource) {
                        log.info("Get sharding datasource!");
                    } else {
                        log.info("Skip to play datasource!");
                        return;
                    }

                    if ("*".equals(dateConfig.getBeginDate())) {
                        log.info("Skip and no begin date");
                        return;
                    }
                    // DataSource dataSource = (DataSource) ((ApplicationReadyEvent) event).getApplicationContext().getBean("dataSource");
                    // 读取扩展配置
                    collectConfig();
                    //spring boot 会使用RestartClassloader加载DateUtil类，但在路由表操作中系统使用的AppClassLoader，
                    //导致静态变量不一致，两个类加载器是各自独立加载该类，加载了两遍
                    //所以这里使用系统的类加载器加载该类进行赋值
                    DateUtil.BEGIN_DATE = DateUtil.toLocalDate(dateConfig.getBeginDate(), "yyyy-MM-dd");

                    if (!"*".equals(dateConfig.getFormat())) {
                        DateUtil.TABLE_NAME_FORMAT = dateConfig.getFormat();
                    }

                    try {
                        Method method1 = ClassLoader.getSystemClassLoader().loadClass("com.blockeng.admin.sharding.DateUtil")
                                .getMethod("setBEGIN_DATE", LocalDate.class);
                        method1.invoke(null, toLocalDate(dateConfig.getBeginDate(), "yyyy-MM-dd"));
                        if (!"*".equals(dateConfig.getFormat())) {
                            Method method2 = ClassLoader.getSystemClassLoader().loadClass("com.blockeng.admin.sharding.DateUtil")
                                    .getMethod("setTABLE_NAME_FORMAT", String.class);
                            method2.invoke(null, dateConfig.getFormat());
                        }
                    } catch (Exception e) {
                        log.info("Jar status !");
                    }

                    log.info("Get ext config {}", extConfig.toString());
                    // 开始工作
                    room.openTheDoor(extConfig, dataSource, loader);
                    room.startWork();
                } catch (Exception e) {
                    log.warn("Start ext sharding failed", e);
                    throw new RuntimeException(e);
                }
            }
            loaded = true;
        }
    }

    /**
     * 聚合配置信息
     */
    private void collectConfig() {
        if (null != ext) {
            ext.forEach((key, value) -> {
                int index = key.indexOf(".");
                String tableKey = key.substring(0, index);
                String configKey = key.substring(index + 1);
                if (extConfig.containsKey(tableKey)) {
                    ExtConfig config = extConfig.get(tableKey);
                    setConfig(configKey, value, config);
                    extConfig.put(tableKey, config);
                } else {
                    ExtConfig config = new ExtConfig();
                    setConfig(configKey, value, config);
                    extConfig.put(tableKey, config);
                    config.setTableName(tableKey);

                }
            });
        }
    }

    //转换配置属性
    private void setConfig(String key, String value, ExtConfig config) {
        if (null != value) {
            switch (key) {
                case "auto-create-table.enable":
                    config.setEnableCreateTable(Boolean.valueOf(value));
                    break;
                case "auto-create-table.sql":
                    config.setCreateTableSQL(value);
                    break;
                case "auto-create-table.create-days":
                    config.setCreateTableDays(Integer.valueOf(value));
                    break;
                case "auto-create-table.create-time":
                    config.setCreateTableTime(value);
                    break;
                case "auto-create-table.route.enable":
                    config.setEnableCreateRoute(Boolean.valueOf(value));
                    break;
                case "auto-create-table.route.create-time":
                    config.setCreateRouteTime(value);
                    break;
                case "auto-create-table.route.create-days":
                    config.setCreateRouteDays(Integer.valueOf(value));
                    break;
                case "auto-create-table.datasource":
                    config.setDataSource(value);
                    break;
            }
        }
    }


    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.loader = resourceLoader;
    }
}
