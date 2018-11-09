package com.blockeng.sharding;

import com.blockeng.sharding.muti.MutiMasterDataSourceConfig;
import com.blockeng.sharding.muti.MutiShardingConfig;
import com.blockeng.sharding.muti.SqlSessionConfig;
import com.blockeng.sharding.muti.TableNodesConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Import({ShardingConfig.class, ShardingAutoTableWorkerRoom.class,
        MasterSlaveConfig.class, BeginDateConfig.class,
        TableNodesConfig.class, MutiShardingConfig.class,
        MutiMasterDataSourceConfig.class, SqlSessionConfig.class})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
/**
 * 外部使用的注解
 */
public @interface EnableExtSharding {

}
