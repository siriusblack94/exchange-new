package com.blockeng.api.sms.smschinese;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * sms.smschinese.url=http://utf8.api.smschinese.cn/
 * sms.smschinese.uid=
 * sms.smschinese.key=
 *
 * @author qiang
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "sms.smschinese")
@Configuration
public class SmsChineseConfig {

    private String url;

    private String uid;

    private String key;
}