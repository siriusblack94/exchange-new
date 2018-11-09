package com.blockeng.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author qiang
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "aliyun.oss.advanced")
public class AliyunOssAdvancedProperties {

    private String protocol;

    private String callbackUrl;

    private String loadUrl;
}
