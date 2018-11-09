package com.blockeng.api.sms.luosimao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * sms.luosimao.url=http://sms-api.luosimao.com/v1/send.json
 * sms.luosimao.api=
 *
 * @author qiang
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "sms.luosimao")
@Configuration
public class LuosimaoConfig {

    private String url;

    private String api;
}
