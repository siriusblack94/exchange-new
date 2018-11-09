package com.blockeng.sharding.muti;

import com.blockeng.sharding.ConfigUtil;
import io.shardingsphere.core.api.MasterSlaveDataSourceFactory;
import io.shardingsphere.core.api.config.MasterSlaveRuleConfiguration;
import io.shardingsphere.core.jdbc.core.ShardingContext;
import io.shardingsphere.core.jdbc.core.datasource.ShardingDataSource;
import io.shardingsphere.jdbc.spring.boot.sharding.SpringBootShardingRuleConfigurationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.util.ReflectionUtils.findField;

@Configuration
@Slf4j
public class MutiShardingConfig {

    @Autowired
    MutiMasterDataSourceConfig masterDataSourceConfig;

    @Autowired
    TableNodesConfig tableNodesConfig;

    @Autowired
    private SpringBootShardingRuleConfigurationProperties shardingProperties;

    @Value("${sharding.jdbc.mutisharding.nodes:*}")
    String mutiDataSrouceNodes;

    @Bean
    @ConditionalOnProperty(name = "sharding.jdbc.mutisharding.enable", havingValue = "true")
    public DataSource mutiShardingDataSource(@Qualifier("dataSource") DataSource datasource) {

        if (datasource instanceof ShardingDataSource && !"*".equals(mutiDataSrouceNodes)) {
            ShardingDataSource shardingDataSource = (ShardingDataSource) datasource;
            Field shardingContextField = findField(ShardingDataSource.class, "shardingContext");
            ReflectionUtils.makeAccessible(shardingContextField);
            ShardingContext context = (ShardingContext) ReflectionUtils.getField(shardingContextField, shardingDataSource);
            Map<String, DataSource> dataSourceMap = context.getDataSourceMap();

            Map<String, MutiMasterInfo> infos = ConfigUtil.propertiesToMap(masterDataSourceConfig.getExt(), MutiMasterInfo.class);
            Map<String, DataSource> masterSlaveDataSourceMap = new HashMap<>();
            infos.forEach((key, info) -> {
                if ("true".equals(info.getIsSlaveSplit())) {
                    try {
                        Map<String, DataSource> newDataSourceMap = new HashMap<>(Map.copyOf(dataSourceMap));
                        Arrays.asList(info.getSlaveName().split(",")).forEach(s -> {
                            newDataSourceMap.put(s, datasource);
                        });
                        DataSource masterSlaveDataSource = MasterSlaveDataSourceFactory.createDataSource(newDataSourceMap,
                                new MasterSlaveRuleConfiguration(
                                        info.getDataSourceName(),
                                        info.getMasterName(),
                                        Arrays.asList(info.getSlaveName().split(",")),
                                        null
                                ),
                                shardingProperties.getConfigMap());
                        masterSlaveDataSourceMap.put(info.getDataSourceName(), masterSlaveDataSource);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        DataSource masterSlaveDataSource = MasterSlaveDataSourceFactory.createDataSource(dataSourceMap,
                                new MasterSlaveRuleConfiguration(
                                        info.getDataSourceName(),
                                        info.getMasterName(),
                                        Arrays.asList(info.getSlaveName().split(",")),
                                        null
                                ),
                                shardingProperties.getConfigMap());
                        masterSlaveDataSourceMap.put(info.getDataSourceName(), masterSlaveDataSource);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            });
            MutiShardingDataSource mutiShardingDataSource = new MutiShardingDataSource();
            Map<String, DataSource> mutiShardingDatasourceMap = new HashMap<>();
            Arrays.asList(mutiDataSrouceNodes.split(",")).forEach(s -> {
                if (masterSlaveDataSourceMap.containsKey(s)) {
                    mutiShardingDatasourceMap.put(s, masterSlaveDataSourceMap.get(s));
                } else if (dataSourceMap.containsKey(s)) {
                    mutiShardingDatasourceMap.put(s, dataSourceMap.get(s));
                }
            });
            Map<String, DataSource> resultDataSourceMap = new HashMap<>();
            tableNodesConfig.getTables().forEach((k, v) -> {
                if (k.endsWith(".actual-data-nodes")) {
                    Arrays.asList(v.split(",")).forEach(s -> {
                        String[] ds = s.split("\\.");
                        String dsName = ds[0];
                        String tbName = ds[1];
                        if (mutiShardingDatasourceMap.containsKey(dsName)) {
                            resultDataSourceMap.put(tbName, mutiShardingDatasourceMap.get(dsName));
                        }
                    });
                }
            });
            mutiShardingDataSource.setDataSourceMap(resultDataSourceMap);
            log.info("Get MutiDataSource :{}", mutiShardingDataSource.toString());
            return mutiShardingDataSource;
        }

        return datasource;
    }
}
