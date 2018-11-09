package com.blockeng.sharding;

import io.shardingsphere.core.api.config.TableRuleConfiguration;
import io.shardingsphere.core.jdbc.adapter.AbstractConnectionAdapter;
import io.shardingsphere.core.jdbc.core.ShardingContext;
import io.shardingsphere.core.jdbc.core.connection.ShardingConnection;
import io.shardingsphere.core.rule.ShardingRule;
import io.shardingsphere.core.rule.TableRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Map;

import static com.blockeng.sharding.DateUtil.*;


/**
 * maple
 */

@Slf4j
public class TableWorker implements ShardingAutoTableWorker {

    private DataSource dataSource;
    private ExtConfig config;
    //   private JdbcTemplate jdbcTemplate;
    private ResourceLoader loader;

    //    private BeginDateConfig dateConfig;
    @Override
    public void run() {
        try {
            checkTable();
        } catch (Exception e) {
            log.warn("Create table failed", e);
        }
    }


    public void checkTable() {
        log.info("Create table start to run for table {}", config.getTableName());
        long howLong = until(BEGIN_DATE, LocalDate.now()) + config.getCreateTableDays();
        //      long howLong = DateUtil.until(DateUtil.toLocalDate(dateConfig.getBeginDate()), LocalDate.now()) + config.getCreateTableDays();
        log.info("Check table status, {} tables should be created", howLong + 1);
        DataSource dataSource = this.dataSource;

//        if (dataSource instanceof MasterSlaveDataSource) {
//            Field dataSourceMapField = ReflectionUtils.findField(MasterSlaveDataSource.class, "dataSourceMap");
//            ReflectionUtils.makeAccessible(dataSourceMapField);
//            Map<String, DataSource> dataSourceMap = (Map<String, DataSource>) ReflectionUtils.getField(dataSourceMapField, dataSource);
//            dataSource = dataSourceMap.get(config.getDataSource());
//        }
        DataSource finalDataSource = dataSource;
        Flux.range(0, (int) howLong + 1).filter(integer ->
                !isTableExist(finalDataSource, config.getTableName() + "_" +
                        (StringUtils.hasText(TABLE_NAME_FORMAT) ? toDateString(BEGIN_DATE.plusDays(integer), TABLE_NAME_FORMAT) : String.valueOf(integer)))
        ).doOnError(throwable -> {
            log.warn("Create table failed", throwable);
        }).subscribeOn(Schedulers.newParallel("Table_check", 4)).subscribe(integer -> {
            Field shardingContextField = ReflectionUtils.findField(finalDataSource.getClass(), "shardingContext");
            ReflectionUtils.makeAccessible(shardingContextField);
            ShardingContext context = (ShardingContext) ReflectionUtils.getField(shardingContextField, finalDataSource);
            ShardingRule shardingRule = context.getShardingRule();
//            ((StandardShardingStrategy) shardingRule.getTableRule("").getTableShardingStrategy()).
            //跟代码后发现，这里ddl语句需要注册LocalTable才可以执行，所以先动态注册，执行建表语句后，再清掉，以免出现别的问题
            TableRuleConfiguration configuration = new TableRuleConfiguration();
            String suffix = "";
            if (StringUtils.hasText(TABLE_NAME_FORMAT))
                suffix = toDateString(BEGIN_DATE.plusDays(integer), TABLE_NAME_FORMAT);
            else
                suffix = String.valueOf(integer);
            configuration.setActualDataNodes(config.getDataSource() + "." + config.getTableName() + "_" + suffix);
            configuration.setLogicTable(config.getTableName() + "_" + suffix);
            TableRule tableRule = new TableRule(configuration, shardingRule.getShardingDataSourceNames());
            shardingRule.getTableRules().add(tableRule);
            //       System.out.println(tableRule);
            createTable(suffix); //建表
            shardingRule.getTableRules().remove(tableRule);
        });
    }

    public void createTable(String suffix) {
        Resource resource = loader.getResource(config.getCreateTableSQL());
        Connection connection = null;
        Statement statement = null;
        DataSource dataSource = this.dataSource;
//        if (dataSource instanceof MasterSlaveDataSource) {
//            Field dataSourceMapField = ReflectionUtils.findField(MasterSlaveDataSource.class, "dataSourceMap");
//            ReflectionUtils.makeAccessible(dataSourceMapField);
//            Map<String, DataSource> dataSourceMap = (Map<String, DataSource>) ReflectionUtils.getField(dataSourceMapField, dataSource);
//            dataSource = dataSourceMap.get(config.getDataSource());
//        }
        try {
            byte[] data = resource.getInputStream().readAllBytes();
            String content = new String(data, "UTF-8");
            content = content.replace("${tableName}", config.getTableName() + "_" + suffix);
            connection = dataSource.getConnection();
            connection = ((ShardingConnection) connection).getShardingContext().getDataSourceMap().get(config.getDataSource()).getConnection();
            statement = connection.createStatement();
            statement.execute(content);
            log.info("Table {} created", config.getTableName() + "_" + suffix);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
                statement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isTableExist(DataSource dataSource, String tableName) {
        ResultSet resultSet = null;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            // connection = ((AbstractConnectionAdapter) connection);
            Method getDataSourceMapMethod = ReflectionUtils.findMethod(AbstractConnectionAdapter.class, "getDataSourceMap");
            ReflectionUtils.makeAccessible(getDataSourceMapMethod);
            Map<String, DataSource> dataSourceMap = (Map<String, DataSource>) ReflectionUtils.invokeMethod(getDataSourceMapMethod, connection);
            connection = dataSourceMap.get(config.getDataSource()).getConnection();
            resultSet = connection.getMetaData().getTables(null, null, tableName, null);
            boolean result = resultSet.next();
            log.info("Check table {} exist:{}", tableName, result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("Check table {} failed", tableName, e);
            return true;
        } finally {
            try {
                connection.close();
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
        this.loader = loader;
    }

//    @Override
//    public void setDateConfig(BeginDateConfig dateConfig) {
//        this.dateConfig =dateConfig;
//    }
}
