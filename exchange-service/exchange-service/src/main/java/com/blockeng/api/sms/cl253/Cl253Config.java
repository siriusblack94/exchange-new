package com.blockeng.api.sms.cl253;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * sms.cl253.url=
 * sms.cl253.account=
 * sms.cl253.password=
 * sms.cl253.app=cl_normal_sms cl_international_sms
 *
 * @author qiang
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "sms.cl253")
@Configuration
public class Cl253Config {

    private String url;

    private String account;

    private String password;

    private String app;
}
