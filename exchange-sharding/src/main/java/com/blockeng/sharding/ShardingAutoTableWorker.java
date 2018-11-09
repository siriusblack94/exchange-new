package com.blockeng.sharding;


import org.springframework.core.io.ResourceLoader;

import javax.sql.DataSource;

public interface ShardingAutoTableWorker extends Runnable {

    void setDataSource(DataSource dataSource);

    void setConfig(ExtConfig value);

    void setResourceLoader(ResourceLoader loader);

//    void setDateConfig(BeginDateConfig dateConfig);
}
