package com.blockeng.sharding.muti;

import com.baomidou.mybatisplus.MybatisConfiguration;
import com.baomidou.mybatisplus.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.mapper.ISqlInjector;
import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import com.baomidou.mybatisplus.spring.boot.starter.ConfigurationCustomizer;
import com.baomidou.mybatisplus.spring.boot.starter.MybatisPlusProperties;
import com.baomidou.mybatisplus.spring.boot.starter.SpringBootVFS;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.List;

/**
 * 重新配置sessionFactory
 */
@Configuration
@EnableConfigurationProperties({MybatisPlusProperties.class})
public class SqlSessionConfig {
    //  private final MybatisPlusProperties properties;
    @Bean
    @ConditionalOnProperty(name = "sharding.jdbc.mutisharding.enable", havingValue = "true")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("mutiShardingDataSource") DataSource dataSource,
                                               @Autowired(required = false) MetaObjectHandler metaObjectHandler,
                                               @Autowired(required = false) IKeyGenerator keyGenerator,
                                               @Autowired(required = false) ISqlInjector iSqlInjector,
                                               MybatisPlusProperties plusProperties,
                                               ResourcePatternResolver loader,
                                               ObjectProvider<Interceptor[]> interceptorsProvider,
                                               ObjectProvider<DatabaseIdProvider> databaseIdProvider,
                                               ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider

    ) throws Exception {

        MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setVfs(SpringBootVFS.class);
        // GlobalConfiguration globalConfig = new GlobalConfiguration();

        if (StringUtils.hasText(plusProperties.getConfigLocation())) {
            factory.setConfigLocation(loader.getResource(plusProperties.getConfigLocation()));
        }
        MybatisConfiguration configuration = plusProperties.getConfiguration();
        if (configuration == null && !StringUtils.hasText(plusProperties.getConfigLocation())) {
            configuration = new MybatisConfiguration();
        }

        if (configuration != null && !CollectionUtils.isEmpty(configurationCustomizersProvider.getIfAvailable())) {
            for (ConfigurationCustomizer customizer : configurationCustomizersProvider.getIfAvailable()) {
                customizer.customize(configuration);
            }
        }
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        factory.setConfiguration(configuration);
        if (plusProperties.getConfigurationProperties() != null) {
            factory.setConfigurationProperties(plusProperties.getConfigurationProperties());
        }
        if (!ObjectUtils.isEmpty(interceptorsProvider.getIfAvailable())) {
            factory.setPlugins(interceptorsProvider.getIfAvailable());
        }
        if (databaseIdProvider.getIfAvailable() != null) {
            factory.setDatabaseIdProvider(databaseIdProvider.getIfAvailable());
        }
        if (StringUtils.hasLength(plusProperties.getTypeAliasesPackage())) {
            factory.setTypeAliasesPackage(plusProperties.getTypeAliasesPackage());
        }
        // TODO 自定义枚举包
        if (StringUtils.hasLength(plusProperties.getTypeEnumsPackage())) {
            factory.setTypeEnumsPackage(plusProperties.getTypeEnumsPackage());
        }
        if (StringUtils.hasLength(plusProperties.getTypeHandlersPackage())) {
            factory.setTypeHandlersPackage(plusProperties.getTypeHandlersPackage());
        }
        if (!ObjectUtils.isEmpty(plusProperties.resolveMapperLocations())) {
            factory.setMapperLocations(plusProperties.resolveMapperLocations());
        }
        GlobalConfiguration globalConfig;
        if (!ObjectUtils.isEmpty(plusProperties.getGlobalConfig())) {
            globalConfig = plusProperties.getGlobalConfig().convertGlobalConfiguration();
        } else {
            globalConfig = new GlobalConfiguration();
        }


//        MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
//        factory.setDataSource(dataSource);
//        factory.setVfs(SpringBootVFS.class);
//        GlobalConfiguration globalConfig = new GlobalConfiguration();
//
//        factory.setMapperLocations(loader.getResources("classpath*:mappers/*.xml"));
//
        factory.setTransactionFactory(new SpringManagedTransactionFactory());
        if (metaObjectHandler != null)
            globalConfig.setMetaObjectHandler(metaObjectHandler);
        if (keyGenerator != null)
            globalConfig.setKeyGenerator(keyGenerator);
        if (iSqlInjector != null)
            globalConfig.setSqlInjector(iSqlInjector);
        factory.setGlobalConfig(globalConfig);


        return factory.getObject();
    }

    @Bean
    @ConditionalOnProperty(name = "sharding.jdbc.mutisharding.enable", havingValue = "true")
    public PlatformTransactionManager platformTransactionManager(@Qualifier("mutiShardingDataSource") DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);
        return dataSourceTransactionManager;
    }
    // @Bean9
    //  @ConditionalOnProperty(name = "sharding.jdbc.mutisharding.enable",havingValue = "true")
    //  public SpringManagedTransactionFactory springManagedTransactionFactory() {
    //      return new SpringManagedTransactionFactory();
    //  }
}
