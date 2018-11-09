package com.blockeng.api.sms.jiuzhouyun;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * sms.jiuzhouyun.url=http://42.51.12.16:7862/sms
 * sms.jiuzhouyun.account=100102
 * sms.jiuzhouyun.password=123456
 *
 * @author qiang
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "sms.jiuzhouyun")
@Configuration
public class JiuZhouYunConfig {

    private String url;

    private String account;

    private String password;
}
