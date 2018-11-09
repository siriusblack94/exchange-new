package com.blockeng.api.sms.m5c;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * sms.m5c.url=https://m.5c.com.cn/api/send/index.php
 * sms.m5c.username=yzgjszb
 * sms.m5c.password=zf68672817
 * sms.m5c.apikey=eec43592b68c884cf0f94e89d586ffc8
 * sms.m5c.encode=UTF-8
 *
 * @author qiang
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "sms.m5c")
@Configuration
public class M5cConfig {

    private String url;

    private String username;

    public String getPassword() {
        return DigestUtils.md5Hex(password);
    }

    private String password;

    private String apikey;

    private String encode;
}
