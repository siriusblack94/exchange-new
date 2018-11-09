package com.blockeng.api.sms.panfeng;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author qiang
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "sms.panfeng")
@Configuration
public class PanfengConfig {

    private String url;

    private String username;

    public String getPasswd() {
        return DigestUtils.md5Hex(passwd);
    }

    private String passwd;
}