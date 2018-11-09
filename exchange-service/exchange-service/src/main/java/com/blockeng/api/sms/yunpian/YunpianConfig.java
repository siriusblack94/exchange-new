package com.blockeng.api.sms.yunpian;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * sms.yunpian.url=https://sms.yunpian.com/v2/sms/single_send.json
 * sms.yunpian.apikey=
 *
 * @author qiang
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "sms.yunpian")
@Configuration
public class YunpianConfig {

    private String url;

    private String apikey;
}
