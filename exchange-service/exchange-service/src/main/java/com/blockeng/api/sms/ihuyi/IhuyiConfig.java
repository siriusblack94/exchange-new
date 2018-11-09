package com.blockeng.api.sms.ihuyi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * sms.ihuyi.url=http://106.ihuyi.com/webservice/sms.php
 * sms.ihuyi.account=
 * sms.ihuyi.password=
 *
 * @author qiang
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "sms.ihuyi")
@Configuration
public class IhuyiConfig {

    private String url;

    private String account;

    private String password;
}
