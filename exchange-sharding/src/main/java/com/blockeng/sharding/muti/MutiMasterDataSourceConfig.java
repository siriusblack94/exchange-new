package com.blockeng.sharding.muti;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "sharding.jdbc.masterslave")
@Data
public class MutiMasterDataSourceConfig {

    Map<String, String> ext;
}
