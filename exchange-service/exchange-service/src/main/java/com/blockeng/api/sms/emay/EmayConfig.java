package com.blockeng.api.sms.emay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * sms.emay.url=
 * sms.emay.appId=
 * sms.emay.gzip=on
 * sms.emay.encode=UTF-8
 *
 * @author qiang
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "sms.emay")
@Configuration
public class EmayConfig {

    private String url;

    private String appId;

    private String gzip;

    private String encode;
}
