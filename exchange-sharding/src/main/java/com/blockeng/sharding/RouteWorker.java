package com.blockeng.sharding;

import io.shardingsphere.core.jdbc.core.ShardingContext;
import io.shardingsphere.core.jdbc.core.datasource.ShardingDataSource;
import io.shardingsphere.core.rule.DataNode;
import io.shardingsphere.core.rule.ShardingRule;
import io.shardingsphere.core.rule.TableRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.blockeng.sharding.DateUtil.*;


@Slf4j
public class RouteWorker implements ShardingAutoTableWorker {

    private DataSource dataSource;

    private ExtConfig config;

//    private BeginDateConfig dateConfig;

    @Override
    public void run() {
        try {
            addRoute();
        } catch (Exception e) {
            log.warn("Add route failed", e);
        }
    }

    public void addRoute() {
        log.info("Add route start to run for table {}", config.getTableName());
        DataSource dataSource = this.dataSource;
//        if (dataSource instanceof MasterSlaveDataSource) {
//            Field dataSourceMapField = ReflectionUtils.findField(MasterSlaveDataSource.class, "dataSourceMap");
//            ReflectionUtils.makeAccessible(dataSourceMapField);
//            Map<String, DataSource> dataSourceMap = (Map<String, DataSource>) ReflectionUtils.getField(dataSourceMapField, dataSource);
//            dataSource = dataSourceMap.get(config.getDataSource());
//        }
        Field shardingContextField = ReflectionUtils.findField(ShardingDataSource.class, "shardingContext");
        ReflectionUtils.makeAccessible(shardingContextField);
        ShardingContext context = (ShardingContext) ReflectionUtils.getField(shardingContextField, dataSource);
        ShardingRule shardingRule = context.getShardingRule();
        TableRule tableRule = shardingRule.getTableRule(config.getTableName());
        //这里的逻辑就是检查从起始日到未来的CreateRouteDays天内的route，如果不存在就添加进去
        List<DataNode> list = tableRule.getActualDataNodes();
        //    long howLong = until(toLocalDate(dateConfig.getBeginDate()), LocalDate.now()) + config.getCreateRouteDays();
        long howLong = until(BEGIN_DATE, LocalDate.now()) + config.getCreateRouteDays();
        log.info("Load last route size is {}, and {} routes should be created", list.size(), howLong + 1);
        List<String> names = list.stream().map(DataNode::getTableName).collect(Collectors.toList());
        Flux.range(0, (int) howLong + 1).doOnError(throwable -> {
            log.warn("Add route failed", throwable);
        }).subscribe(integer -> {
            String suffix = "";
            if (StringUtils.hasText(TABLE_NAME_FORMAT))
                suffix = toDateString(BEGIN_DATE.plusDays(integer), TABLE_NAME_FORMAT);
            else
                suffix = String.valueOf(integer);
            if (!names.contains(config.getTableName() + "_" + integer)) {
                log.info("Add route {}", config.getTableName() + "_" + suffix);
                list.add(new DataNode(config.getDataSource(), config.getTableName() + "_" + suffix));
            }
        });
    }

    @Override
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void setConfig(ExtConfig config) {
        this.config = config;
    }

    @Override
    public void setResourceLoader(ResourceLoader loader) {

    }

//    @Override
//    public void setDateConfig(BeginDateConfig dateConfig) {
//        this.dateConfig =dateConfig;
//    }
}
