package com.blockeng.sharding;

import io.shardingsphere.core.api.ShardingDataSourceFactory;
import io.shardingsphere.core.jdbc.core.datasource.MasterSlaveDataSource;
import io.shardingsphere.jdbc.spring.boot.sharding.SpringBootShardingRuleConfigurationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Map;

@Configuration
@Slf4j
public class MasterSlaveConfig {

    @Autowired
    private SpringBootShardingRuleConfigurationProperties shardingProperties;


    @Value("${sharding.jdbc.datasource.ext.is-master-full-slave-split:false}")
    private boolean isMasterFullSlaveSplit;

    @Value("${sharding.jdbc.config.masterslave.slave-data-source-names:*}")
    private String slaves;

    @Bean
    public DataSource shardingSlaveDatasource(@Qualifier("dataSource") DataSource datasource) {
        if (datasource instanceof MasterSlaveDataSource && isMasterFullSlaveSplit) {
            Assert.isTrue(!"*".equals(slaves), "No slave data sources");
            try {
                Map<String, DataSource> masterSlaveDataSourceMap = ((MasterSlaveDataSource) datasource).getDataSourceMap();
                Map<String, DataSource> shardingDataSourceMap = Map.copyOf(((MasterSlaveDataSource) datasource).getDataSourceMap());
                DataSource dataSource = ShardingDataSourceFactory.createDataSource(shardingDataSourceMap,
                        shardingProperties.getShardingRuleConfiguration(), shardingProperties.getConfigMap(), shardingProperties.getProps());
                Arrays.asList(slaves.split(",")).stream().forEach(s -> {
                    masterSlaveDataSourceMap.put(s, dataSource);
                });
                return dataSource;
            } catch (Exception e) {
                log.info("", e);
            }
        }
        return datasource;
    }
}
